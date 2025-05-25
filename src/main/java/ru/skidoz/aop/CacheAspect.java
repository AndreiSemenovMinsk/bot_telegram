package ru.skidoz.aop;

import static org.springframework.util.CollectionUtils.isEmpty;

import ru.skidoz.aop.repo.JpaRepositoryTest;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.DTO;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import ru.skidoz.model.entity.AbstractEntity;
import jakarta.annotation.PostConstruct;
import org.reflections.Reflections;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


@Component
public class CacheAspect {

    ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock writeLock = lock.writeLock();
    Lock readLock = lock.readLock();

    public Map<String, Integer> repos = new HashMap<>();

    List<ConcurrentHashMap<Integer, DTO> > idMap = new ArrayList<>();

    public List<ConcurrentHashMap<Integer, DTO>> idNewMap = new ArrayList<>();
    //порядок из parameters
    List<List<ConcurrentHashMap<String, Object>>> mapRepos = new ArrayList<>();

    List<Map<Integer, List<String>>> dtoKeys = new ArrayList<>();
    List<List<Map<Integer, Set<Integer>>>> mapDependant = new ArrayList<>();

    //List<List<ConcurrentHashMap<String, Object>>> mapFields = new ArrayList<>();
    List<List<String[]>> parameters = new ArrayList<>();
    List<List<Integer[]>> parameterDTOFieldInds = new ArrayList<>();
    //порядковый номер метода по названию
    List<Map<String, Integer>> methodOrders = new ArrayList<>();

    List<List<Integer>> collectionType = new ArrayList<>();


    List<String> dtoNames = new ArrayList<>();
    Map<String, Integer> dtoNamePlaces = new HashMap<>();
    List<String[]> dtoFieldNames = new ArrayList<>();
    List<String[]> dtoFieldCollectionNames = new ArrayList<>();
    List<Integer[]> dtoFieldCollectionOrder = new ArrayList<>();
    List<Map<String, Integer>> dtoFieldsMap = new ArrayList<>();
    //List<List<Integer>> dtoFieldNames = new ArrayList<>();

    List<List<Integer>> parentDTOChildDTOIndex = new ArrayList<>();
    List<List<Integer>> parentDTOChildDTOFieldIndex = new ArrayList<>();
    List<List<List<Integer>>> parentDTOChildMethodIndex = new ArrayList<>();
    List<List<List<Integer>>> parentDTOChildParamIndex = new ArrayList<>();

    List<List<Integer>> childDTOFieldParentDTOIndex = new ArrayList<>();
    List<List<Integer>> childDTOFieldParentFieldDTOIndex = new ArrayList<>();
    List<List<Integer>> childDTOFieldParentFieldIndex = new ArrayList<>();

    List<List<List<Integer>>> DTOFieldParamIndex = new ArrayList<>();
    List<List<List<Integer>>> DTOFieldMethodParamDTOFieldIndex = new ArrayList<>();
    List<List<List<Integer>>> DTOFieldMethodIndex = new ArrayList<>();


    List<List<List<Integer>>> childDTOMethodParentDTOList = new ArrayList<>();
    List<List<List<Integer>>> childDTOMethodParentMethodRequestList = new ArrayList<>();
    List<List<List<Integer>>> childDTOMethodParentFieldDTOList = new ArrayList<>();

    List<List<List<Integer>>> childDTOMethodParentMethodMethodList = new ArrayList<>();
    List<List<List<List<Integer>>>> childDTOMethodParentMethodRequestParamsList = new ArrayList<>();
    List<List<List<Integer>>> childDTOMethodParentMethodDTOList = new ArrayList<>();
    List<List<Integer[]>> DTOParamFieldIndex = new ArrayList<>();
    List<List<Method>> jpaFindMethods = new ArrayList<>();

    //надо придумать использование
    List<List<List<Integer>>> DTOParamFreeFieldIndex = new ArrayList<>();
    //
    List<List<Integer>> DTOMethodDependentToIndependent = new ArrayList<>();

    List<JpaRepository<?, Integer>> jpaRepositories = new ArrayList<>();
    List<EntityMapper> mapRepositories = new ArrayList<>();


    private ApplicationContext context;

    public CacheAspect(ApplicationContext context) throws NoSuchMethodException {
        System.out.println("45465465464654654654655");

        this.context = context;
        ee();
    }

    @PostConstruct
    public void uuu() {
        Reflections jpaReflections = new Reflections("ru.skidoz.repository");
        Set<Class<? extends JpaRepository>> subTypes = jpaReflections.getSubTypesOf(JpaRepository.class);
        for (Class<? extends JpaRepository> interf : subTypes) {
            if (interf.isInterface()) {

                if (interf.getGenericInterfaces()[0] instanceof ParameterizedType parameterizedType) {
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    Class<?> clazz = (Class<?>) actualTypeArguments[0];
                    String dtoName = clazz.getSimpleName();

                    Integer dtoIndex = dtoNamePlaces.get(dtoName.substring(0, dtoName.length() - "Entity".length()));

                    if (dtoIndex != null) {
                        //System.out.println();
                        //System.out.println("@*@*@* " + firstLower(interf.getSimpleName()));

                        JpaRepository<?, Integer> myBean = (JpaRepository<?, Integer>) context
                                .getBean(firstLower(interf.getSimpleName()));

                        //System.out.println(dtoNames.get(dtoIndex) + " **@#*** " + myBean);

                        jpaRepositories.set(dtoIndex, myBean);
                    }
                }
            }
        }


        Reflections mapReflections = new Reflections("ru.skidoz.mappers");
        Set<Class<? extends EntityMapper>> mapTypes = mapReflections.getSubTypesOf(EntityMapper.class);
        for (Class<? extends EntityMapper> interf : mapTypes) {

            if (interf.getGenericSuperclass() instanceof ParameterizedType parameterizedType) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                Class<?> clazz = (Class<?>) actualTypeArguments[0];
                String dtoName = clazz.getSimpleName();
                Integer dtoIndex = dtoNamePlaces.get(dtoName);

                if (dtoIndex != null) {
                    EntityMapper myBean = (EntityMapper) context
                            .getBean(firstLower(interf.getSimpleName() + "Impl"));

                    mapRepositories.set(dtoIndex, myBean);
                }
            }
        }
    }

    public void ee() throws NoSuchMethodException {
        Reflections cacheReflections = new Reflections("ru.skidoz.aop.repo");
        Set<Class<? extends JpaRepositoryTest>> cacheSubTypes = cacheReflections
                .getSubTypesOf(JpaRepositoryTest.class);

        ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) context;
        ConfigurableListableBeanFactory beanFactory = configurableContext.getBeanFactory();

        System.out.println("ee()");

        List<Class<? extends JpaRepositoryTest>> list = new ArrayList<>();
        List<Class<?>> listDTO = new ArrayList<>();
        List<List<String>> implJpaMethodNames = new ArrayList<>();
        List<String> typeNames = new ArrayList<>();

        int i = 0;
        for (Class<? extends JpaRepositoryTest> interf : cacheSubTypes) {

            if (interf.isInterface()) {

                if (interf.getGenericInterfaces()[0] instanceof ParameterizedType parameterizedType) {
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

                    Class<?> clazz = (Class<?>) actualTypeArguments[0];

                    listDTO.add(clazz);
                    dtoNamePlaces.put(clazz.getSimpleName(), i);
                    i++;

                    List<String> methodHashes = new ArrayList<>();
                    implJpaMethodNames.add(methodHashes);

                    for (Method declaredMethod : interf.getDeclaredMethods()) {

                        if (Arrays.stream(declaredMethod.getParameters())
                                .noneMatch(t -> t.getType().getSimpleName().equals("Object"))) {
                            String hash = declaredMethod.getName() + ":" +
                                    Arrays.stream(declaredMethod.getParameters())
                                            .map(t -> t.getType().getSimpleName())
                                            .collect(Collectors.joining(","));

                            methodHashes.add(hash);
                        }
                    }

                    JpaRepositoryTest e = createProxy(interf);

                    beanFactory.registerSingleton(interf.getSimpleName(), e);

                    list.add(e.getClass());
                    typeNames.add(interf.getSimpleName());
                }
            }
        }

        List<Map<String, Method>> jpaMethods = new ArrayList<>();
        for (i = 0; i < listDTO.size(); i++) {
            jpaMethods.add(new HashMap<>());
            jpaRepositories.add(null);
            mapRepositories.add(null);
        }

        Reflections jpaReflections = new Reflections("ru.skidoz.repository");
        Set<Class<? extends JpaRepository>> subTypes = jpaReflections
                .getSubTypesOf(JpaRepository.class);

        for (Class<? extends JpaRepository> interf : subTypes) {
            if (interf.isInterface()) {

                if (interf.getGenericInterfaces()[0] instanceof ParameterizedType parameterizedType) {
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    Class<?> clazz = (Class<?>) actualTypeArguments[0];
                    String dtoName = clazz.getSimpleName();

                    Integer dtoIndex = dtoNamePlaces.get(dtoName.substring(0, dtoName.length() - "Entity".length()));


                    if (dtoIndex != null) {

                        for (Method declaredMethod : interf.getDeclaredMethods()) {
                            if (Arrays.stream(declaredMethod.getParameters())
                                    .noneMatch(t -> t.getType().getSimpleName().equals("Object"))) {

                                String methodName = declaredMethod.getName()
                                    .replaceAll("_Id", "")
                                    .replaceAll("Entity", "");

                                System.out.println(declaredMethod.getName() + "---*)*)*)*)---" + methodName);

                                String hash = methodName + ":" +
                                        Arrays.stream(declaredMethod.getParameters())
                                                .map(t -> {
                                                    String simpleName = t.getType().getSimpleName();
                                                    if (simpleName.endsWith("Entity")) {
                                                        return simpleName.substring(0,
                                                                simpleName.length() - "Entity".length());
                                                    } else {
                                                        return simpleName;
                                                    }
                                                })
                                                .collect(Collectors.joining(","));

                                jpaMethods.get(dtoIndex).put(hash, declaredMethod);
                            }
                        }
                    }
                }
            }
        }
        init(list, listDTO, implJpaMethodNames, typeNames, jpaMethods);
    }

    private JpaRepositoryTest createProxy(Class<? extends JpaRepositoryTest> interfaceClass) {
        // Создаем реальный объект, реализующий интерфейс <Class<? extends JpaRepositoryTest>>
        return (JpaRepositoryTest<?, ?>) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        var repoFile = interfaceClass.getSimpleName();

                        var repoIndex = repos.get(repoFile);
                        var methodName = method.getName();

                        return switch (methodName) {
                            case "save" -> {
                                try {
                                    writeLock.lock();
                                    yield save((DTO) args[0], repoIndex);
                                } finally {
                                    writeLock.unlock();
                                }
                            }
                            case "cache" -> {
                                try {
                                    writeLock.lock();
                                    yield cache((DTO) args[0], repoIndex);
                                } finally {
                                    writeLock.unlock();
                                }
                            }
                            case "findById" -> {
                                try {
                                    readLock.lock();
                                    yield processFindById(repoIndex, (int) args[0]);
                                } finally {
                                    readLock.unlock();
                                }
                            }
                            case "findAll" -> {
                                try {
                                    readLock.lock();
                                    yield processFindAll(repoIndex);
                                } finally {
                                    readLock.unlock();
                                }
                            }
                            case "deleteById" -> {
                                try {
                                    writeLock.lock();
                                    yield deleteById(repoIndex, (int) args[0]);
                                } finally {
                                    writeLock.unlock();
                                }
                            }
                            case "delete" -> {
                                try {
                                    writeLock.lock();
                                    yield deleteById(repoIndex, ((DTO) args[0]).getId());
                                } finally {
                                    writeLock.unlock();
                                }
                            }
                            case "hashCode" -> this.hashCode();
                            case "equals" -> this.equals(args[0]);
                            case "toString" -> this.toString();
                            default -> {
                                if (methodName.startsWith("findBy")) {
                                    try {
                                        readLock.lock();
                                        yield processFind(repoIndex, methodName, args);
                                    } finally {
                                        readLock.unlock();
                                    }
                                } else if (methodName.startsWith("findAllBy")) {
                                    try {
                                        readLock.lock();
                                        yield processFindAll(repoIndex);
                                    } finally {
                                        readLock.unlock();
                                    }
                                }
                                throw new IllegalArgumentException("Unsupported method: " + methodName + " " + Arrays.toString(args));
                            }
                        };
                    }
                }
        );
    }

    String[][] listDtos = null;

    //@PostConstruct
    public void init(List<Class<? extends JpaRepositoryTest>> subTypes,
                     List<Class<?>> dtoTypes,
                     List<List<String>> implJpaMethodNames,
                     List<String> typeNames,
                     List<Map<String, Method>> jpaMethods
    ) throws NoSuchMethodException {

        Field[][] declaredFieldsDTO = new Field[subTypes.size()][];

        int repoIndex = 0;
        for (var typeName : typeNames) {

            repos.put(typeName, repoIndex);

//            System.out.println("typeName++++" + typeName);

            mapRepos.add(new ArrayList<>());
            idMap.add(new ConcurrentHashMap<>(1024));
            idNewMap.add(new ConcurrentHashMap<>(1024));
            parameters.add(new ArrayList<>());
            parameterDTOFieldInds.add(new ArrayList<>());
            methodOrders.add(new HashMap<>());
            dtoFieldsMap.add(new HashMap<>());
            collectionType.add(new ArrayList<>());
            dtoKeys.add(new HashMap<>());
            jpaFindMethods.add(new ArrayList<>());

            List<List<Integer>> parentDTOFieldChildDTOMethod = new ArrayList<>();
            List<List<Integer>> parentDTOFieldChildDTOParam = new ArrayList<>();
            var ee = new ArrayList<Map<Integer, Set<Integer>>>();
            for (int eeI = 0; eeI < subTypes.size(); eeI++) {
                ee.add(new HashMap<>(256));
                parentDTOFieldChildDTOMethod.add(new ArrayList<>());
                parentDTOFieldChildDTOParam.add(new ArrayList<>());
            }
            mapDependant.add(ee);

            parentDTOChildMethodIndex.add(parentDTOFieldChildDTOMethod);
            parentDTOChildParamIndex.add(parentDTOFieldChildDTOParam);

            List<Integer> parentDTOFieldChildDTO = new ArrayList<>();
            List<Integer> parentDTOFieldChildField = new ArrayList<>();
            List<Integer> childDTOFieldParentDTO = new ArrayList<>();
            List<Integer> childDTOFieldParentFieldDTO = new ArrayList<>();
            List<Integer> childDTOFieldParentField = new ArrayList<>();

            Field[] declaredFields = null;

            declaredFields = dtoTypes.get(repoIndex).getDeclaredFields();
            declaredFieldsDTO[repoIndex] = declaredFields;

            dtoNames.add(firstLower(dtoTypes.get(repoIndex).getSimpleName()));

            List<String> a = new ArrayList<>();

            dtoFieldsMap.get(repoIndex).put("id", 0);
            childDTOFieldParentDTO.add(null);
            childDTOFieldParentFieldDTO.add(null);
            childDTOFieldParentField.add(null);

            int i = 0;
            for (int declaredFieldInd = 0; declaredFieldInd < declaredFields.length; declaredFieldInd++) {
//                        if ("List".equals(declaredFields[declaredFieldInd].getType().getSimpleName())) {
//
//                        }
                var dtoFieldName = declaredFields[declaredFieldInd].getName();
                a.add(dtoFieldName);

                dtoFieldsMap.get(repoIndex).put(dtoFieldName, i);

                childDTOFieldParentDTO.add(null);
                childDTOFieldParentFieldDTO.add(null);
                childDTOFieldParentField.add(null);
                i++;
                //}
            }

            //repo - dto
            //dtoFieldNames.add(a);
            dtoFieldNames.add(a.toArray(new String[0]));

            parentDTOChildDTOIndex.add(parentDTOFieldChildDTO);
            parentDTOChildDTOFieldIndex.add(parentDTOFieldChildField);

            childDTOFieldParentDTOIndex.add(childDTOFieldParentDTO);
            childDTOFieldParentFieldDTOIndex.add(childDTOFieldParentDTO);
            childDTOFieldParentFieldIndex.add(childDTOFieldParentDTO);

            //для id
            mapRepos.get(repoIndex).add(new ConcurrentHashMap<>(1024));

            repoIndex++;
        }
        //processParentInit();

        listDtos = new String[dtoFieldNames.size()][dtoFieldNames.size()];

        repoIndex = 0;
        for (var subtype : subTypes) {

            DTOParamFieldIndex.add(new ArrayList<>());
            DTOParamFreeFieldIndex.add(new ArrayList<>());
            DTOMethodDependentToIndependent.add(new ArrayList<>());

            childDTOMethodParentDTOList.add(new ArrayList<>());
            childDTOMethodParentFieldDTOList.add(new ArrayList<>());
            childDTOMethodParentMethodRequestList.add(new ArrayList<>());

            childDTOMethodParentMethodMethodList.add(new ArrayList<>());

            childDTOMethodParentMethodRequestParamsList.add(new ArrayList<>());

            childDTOMethodParentMethodDTOList.add(new ArrayList<>());


            var declaredFields = declaredFieldsDTO[repoIndex];
            List<Method> implMethods = new ArrayList<>();
            List<String> hashes = new ArrayList<>();

            for (Method declaredMethod : subtype.getDeclaredMethods()) {

                String hash = declaredMethod.getName() + ":" +
                        Arrays.stream(declaredMethod.getParameters())
                                .map(t -> t.getType().getSimpleName())
                                .collect(Collectors.joining(","));

                if (implJpaMethodNames.get(repoIndex).contains(hash)) {
                    implMethods.add(declaredMethod);
                    implJpaMethodNames.get(repoIndex).remove(hash);

                    hashes.add(hash);
                }
            }

            Method jpaMethod = jpaMethods.get(repoIndex).get("findById:Integer");

            Method cacheMethod = subtype.getMethod("findById", Object.class);

            processMethod("findById", repoIndex, jpaMethod, 0, cacheMethod);

            //теперь анализируем репозитории и методы поиска
            int methodInd = 1;
            int hashInd = 0;
            for (var method : implMethods) {
                var findMethodName = method.getName();

                String hash = hashes.get(hashInd);

                System.out.println("_+__+_+" + dtoNames.get(repoIndex) + "." + findMethodName);
                System.out.println(hash + "++hashes+++" + jpaMethods.get(repoIndex).get(hash));
                System.out.println("*** " + jpaMethods.get(repoIndex));
                System.out.println(jpaMethods.get(repoIndex).containsKey(hash));

                jpaMethods.get(repoIndex).keySet()
                        .forEach(k -> System.out.println("*" + k + "*--" + k.equals(hash)));

                jpaMethod = jpaMethods.get(repoIndex).get(hash);
                hashInd++;

                if (findMethodName.equals("findById")
                        || !findMethodName.startsWith("findBy")
                        || findMethodName.length() < 7) {
                    continue;
                }

                processMethod(findMethodName, repoIndex, jpaMethod, methodInd, method);

                methodInd++;
            }

            var parameter = parameters.get(repoIndex);
            List<List<Integer>> parentFieldParamIndex = new ArrayList<>();
            List<List<Integer>> parentFieldFindMethodIndex = new ArrayList<>();

            List<String> fieldNames = new ArrayList<>();
            List<Integer> order = new ArrayList<>();


            for (int declaredFieldInd = 0; declaredFieldInd < declaredFields.length; declaredFieldInd++) {


//                System.out.println("declaredFields[" + declaredFieldInd + "]++++++" +
//                        declaredFields[declaredFieldInd].getType().getSimpleName());

                if ("List".equals(declaredFields[declaredFieldInd].getType().getSimpleName())) {

                    Type genericFieldType = declaredFields[declaredFieldInd].getGenericType();

                    if (genericFieldType instanceof ParameterizedType aType) {

                        final String typeName = aType.getActualTypeArguments()[0].getTypeName();

                        final String simpleName = firstLower(typeName.substring(typeName.lastIndexOf(".") + 1));

                        final int index = dtoNames.indexOf(simpleName);
                        if (index > -1) {
                            order.add(index);
                            fieldNames.add(declaredFields[declaredFieldInd].getName());


                            listDtos[repoIndex][index] = declaredFields[declaredFieldInd].getName();
                        }
                    }

                }

                var dtoFieldName = declaredFields[declaredFieldInd].getName();

                List<Integer> paramIndex = new ArrayList<>();
                List<Integer> findMethodIndex = new ArrayList<>();
                for (methodInd = 0; methodInd < parameter.size(); methodInd++) {

                    for (int parInd = 0; parInd < parameter.get(methodInd).length; parInd++) {

                        if (parameter.get(methodInd)[parInd] != null
                                && parameter.get(methodInd)[parInd].equals(dtoFieldName)) {
                            paramIndex.add(parInd);
                            findMethodIndex.add(methodInd);
                            break;
                        }
                    }
                }
                parentFieldParamIndex.add(paramIndex);
                parentFieldFindMethodIndex.add(findMethodIndex);
            }

            dtoFieldCollectionNames.add(fieldNames.toArray(new String[0]));
            dtoFieldCollectionOrder.add(order.toArray(new Integer[0]));

            // совместное использование
            DTOFieldParamIndex.add(parentFieldParamIndex);
            DTOFieldMethodIndex.add(parentFieldFindMethodIndex);

            repoIndex++;
        }

        //проходим по dto - методам и всем зависимым по полям
        for (int childInd = 0; childInd < childDTOMethodParentDTOList.size(); childInd++) {
            for (int childMethodInd = 0; childMethodInd < childDTOMethodParentDTOList.get(childInd).size(); childMethodInd++) {

                //уровень метода в репозитории - список родительских dto
                var parentDTOInd = childDTOMethodParentDTOList.get(childInd).get(childMethodInd);
                //уровень метода в репозитории - список родительских филдов - парно с dto
                var parentDTOFieldInd = childDTOMethodParentFieldDTOList.get(childInd).get(childMethodInd);

                var parentDTORequestParamInd = childDTOMethodParentMethodRequestList.get(childInd).get(childMethodInd);

//                System.out.println(dtoNames.get(childInd) + " +++++parentDTOInd " + childInd);
//                for (Integer i : parentDTOInd) {
//                    System.out.println(i + " @-+-@ " + dtoNames.get(i));
//                }

                if (!isEmpty(parentDTOInd)) {
//                    if (collectionType.get(childInd).get(childMethodInd) == 0) {
//                        collectionType.get(childInd).set(childMethodInd, 1);
//                    }
                    List<Integer> parentDTOList = new ArrayList<>();
                    List<List<Integer>> parentDTOFieldList = new ArrayList<>();
                    List<List<Integer>> parentDTORequestParamList = new ArrayList<>();

                    //формирование структуры parent [parent-dto][parent-field]
                    for (int k = 0; k < parentDTOInd.size(); k++) {

                        int parentInd = parentDTOInd.get(k);
                        int parentFieldInd = parentDTOFieldInd.get(k);
                        int parentRequestInd = parentDTORequestParamInd.get(k);

//                        System.out.println("parentDTOList+++++" + parentDTOList
//                                + " parentDTOInd.get("+k+") " +parentDTOInd.get(k));


                        var indexInList = parentDTOList.indexOf(parentDTOInd.get(k));
                        if (indexInList == -1) {
                            indexInList = parentDTOList.size();
                            parentDTOList.add(parentInd);
                            parentDTOFieldList.add(new ArrayList<>());
                            parentDTORequestParamList.add(new ArrayList<>());
                        }
                        parentDTOFieldList.get(indexInList).add(parentFieldInd);
                        parentDTORequestParamList.get(indexInList).add(parentRequestInd);
                    }

                    var childFields = new ArrayList<Integer>();
                    var paramFieldIndex = DTOParamFieldIndex.get(childInd).get(childMethodInd);

//                    System.out.println("DTOParamFieldIndex.get("+childInd+") " + DTOParamFieldIndex.get(childInd));
//                    System.out.println("paramFieldIndex " + Arrays.toString(paramFieldIndex));

                    for (int f = 0; f < paramFieldIndex.length; f++) {
                        if (paramFieldIndex[f] != null) {
                            childFields.add(paramFieldIndex[f]);
                        }
                    }

//                    System.out.println("childFields--+--" + childFields);

                    boolean noSuitableChildMethod = true;
                    for (int childMethodIndVariant = 0;
                         childMethodIndVariant < parameterDTOFieldInds.get(childInd).size();
                         childMethodIndVariant++) {
                        Integer[] params = parameterDTOFieldInds.get(childInd).get(childMethodIndVariant);
                        if (params.length == childFields.size()) {
                            boolean cont = true;
                            for (var param : params) {
                                if (!childFields.contains(param)) {
                                    cont = false;
                                    break;
                                }
                            }
                            //если нашли подходящий метод в дочернем
                            if (cont) {
                                DTOMethodDependentToIndependent.get(childInd)
                                        .set(childMethodInd, childMethodIndVariant);

                                noSuitableChildMethod = false;
                                break;
                            }
                        }
                    }

                    if (noSuitableChildMethod) {

                        Integer[] childFieldsArr = new Integer[childFields.size()];
                        String[] fieldNames = new String[childFields.size()];

                        for (int u = 0; u < childFields.size(); u++) {
                            var childField = childFields.get(u);

                            childFieldsArr[u] = childField;

                            var fieldName = dtoFieldNames.get(childInd)[childField];
                            fieldNames[u] = fieldName;
                        }

                        int newMethodIndex = parameters.get(childInd).size();

                        parameterDTOFieldInds.get(childInd).add(childFieldsArr);
                        parameters.get(childInd).add(fieldNames);
                        mapRepos.get(childInd).add(new ConcurrentHashMap<>());
                        collectionType.get(childInd).add(1);

//                        System.out.println("635 collectionType.get("+childInd+")[" + (collectionType.get(childInd).size() -1) + "]"
//                                + collectionType.get(childInd).get(collectionType.get(childInd).size() - 1));

                        DTOParamFreeFieldIndex.get(childInd).add(Arrays.asList(childFieldsArr));

                        DTOMethodDependentToIndependent.get(childInd).set(childMethodInd, newMethodIndex);

                        StringJoiner childJoiner = new StringJoiner("And");
                        for (var requestParam : childFieldsArr) {
                            childJoiner.add(requestParam.toString());
                        }
                        var findMethodName = "findBy" + childJoiner.toString();
                        methodOrders.get(childInd).put(findMethodName, newMethodIndex);
                    }

                    //перебор всех родительских репозиториев для одного дочернего метода
                    for (int k = 0; k < parentDTOList.size(); k++) {
                        //поиск подходящего метода в родительском репозитории
                        int parentInd = parentDTOList.get(k);
                        List<Integer> parentFields = parentDTOFieldList.get(k);
                        List<Integer> parentRequests = parentDTORequestParamList.get(k);

                        boolean noSuitableParentMethod = true;
                        for (int m = 0; m < parameterDTOFieldInds.get(parentInd).size(); m++) {
                            Integer[] params = parameterDTOFieldInds.get(parentInd).get(m);
                            if (params.length == parentFields.size()) {
                                boolean cont = true;
                                for (var param : params) {
                                    if (!parentFields.contains(param)) {
                                        cont = false;
                                        break;
                                    }
                                }
                                //если нашли подходящий метод в родительском
                                if (cont) {
                                    childDTOMethodParentMethodDTOList.get(childInd).get(childMethodInd)
                                            .add(parentInd);

                                    childDTOMethodParentMethodMethodList.get(childInd).get(childMethodInd)
                                            .add(m);

                                    childDTOMethodParentMethodRequestParamsList.get(childInd).get(childMethodInd)
                                            .add(parentRequests);

                                    noSuitableParentMethod = false;
                                }
                            }
                        }

                        if (noSuitableParentMethod) {
                            Integer[] parentFieldsArr = new Integer[parentFields.size()];
                            String[] fieldNames = new String[parentFields.size()];

                            for (int u = 0; u < parentFields.size(); u++) {
                                var parentField = parentFields.get(u);

                                parentFieldsArr[u] = parentField;

                                var fieldName = dtoFieldNames.get(parentInd)[parentField];
                                fieldNames[u] = fieldName;
                            }

                            parameterDTOFieldInds.get(parentInd).add(parentFieldsArr);
                            parameters.get(parentInd).add(fieldNames);
                            mapRepos.get(parentInd).add(new ConcurrentHashMap<>());
                            collectionType.get(parentInd).add(1);

//                            System.out.println("702 collectionType.get("+parentInd+")[" + (collectionType.get(parentInd).size() -1) + "]"
//                                    + collectionType.get(parentInd).get(collectionType.get(parentInd).size() - 1));

                            DTOParamFreeFieldIndex.get(parentInd).add(Arrays.asList(parentFieldsArr));


                            StringJoiner childJoiner = new StringJoiner("And");
                            for (var requestParam : parentFieldsArr) {
                                childJoiner.add(requestParam.toString());
                            }
                            var findMethodName = "findBy" + childJoiner.toString();
                            methodOrders.get(parentInd).put(findMethodName, parameters.get(childInd).size());


                            childDTOMethodParentMethodDTOList.get(childInd).get(childMethodInd)
                                    .add(parentInd);

                            childDTOMethodParentMethodMethodList.get(childInd).get(childMethodInd)
                                    .add(parameterDTOFieldInds.get(parentInd).size() - 1);

                            childDTOMethodParentMethodRequestParamsList.get(childInd).get(childMethodInd)
                                    .add(parentRequests);
                        }
                    }
                }
            }
        }
        processParentDTO();
    }








    private void processMethod(String findMethodName,
                               int repoIndex,
                               Method jpaMethod,/*List<Map<String, Method>> jpaMethods,*/
                               int methodInd,
                               Method method) {

        childDTOMethodParentDTOList.get(repoIndex).add(new ArrayList<>());
        childDTOMethodParentFieldDTOList.get(repoIndex).add(new ArrayList<>());
        childDTOMethodParentMethodRequestList.get(repoIndex).add(new ArrayList<>());

        childDTOMethodParentMethodMethodList.get(repoIndex).add(new ArrayList<>());
        childDTOMethodParentMethodRequestParamsList.get(repoIndex).add(new ArrayList<>());
        childDTOMethodParentMethodDTOList.get(repoIndex).add(new ArrayList<>());

        String[] fields = findMethodName.substring(6).split("And");
        String[] fieldsLower = new String[fields.length];
        Integer[] paramFieldIndex = new Integer[fields.length];
        List<Integer> paramFieldIndexDependantFree = new ArrayList<>();


        System.out.println(dtoNames.get(repoIndex) + " 832c " + jpaMethod);

        jpaFindMethods.get(repoIndex).add(jpaMethod);


        boolean dependsOnParentField = false;
        for (int fieldIndexFromDto = 0; fieldIndexFromDto < fields.length; fieldIndexFromDto++) {

            String fieldName = firstLower(fields[fieldIndexFromDto]);


//                System.out.println(dtoNames.get(repoIndex) + " dtoFieldsMap.get("+repoIndex+").get("+fieldName+") "
//                        + dtoFieldsMap.get(repoIndex).get(fieldName));

            paramFieldIndex[fieldIndexFromDto] = dtoFieldsMap.get(repoIndex).get(fieldName);

            //ищем зависимости по полям
            if (paramFieldIndex[fieldIndexFromDto] == null) {
                dependsOnParentField = true;
                //ищем подходящий родительский класс
                for (int parentDTOInd = 0; parentDTOInd < dtoNames.size(); parentDTOInd++) {
                    var dtoName = dtoNames.get(parentDTOInd);


//                        System.out.println("fieldName " + fieldName + " dtoName " + dtoName
//                                + "***" + fieldName.startsWith(dtoName));

                    if (fieldName.startsWith(dtoName)) {


                        System.out.println("@_@****---" + dtoName + "+fieldName-" + fieldName
                        + " repoIndex@@ " + dtoNames.get(repoIndex) + " --- " + findMethodName);

                        Integer parentField;
                        if (dtoName.length() == fieldName.length()) {

                            parentField = 0;
                        } else {
                            //ищем поле в родительском классе
                            String childFieldNameRest;
                            if (fieldName.charAt(dtoName.length()) == '_') {
                                childFieldNameRest = fieldName.substring(dtoName.length() + 1);
                            } else {
                                childFieldNameRest = fieldName.substring(dtoName.length());
                            }
//                            System.out.println("-----childFieldNameRest " + childFieldNameRest);
                            parentField =
                                    dtoFieldsMap.get(parentDTOInd).get(firstLower(childFieldNameRest));
                        }

//                            System.out.println(" dtoFieldsMap.get(" + parentDTOInd+ ") "
//                                    + dtoFieldsMap.get(parentDTOInd)
//                                    + "   firstLower(childFieldNameRest) " + firstLower(childFieldNameRest)
//                                    + " parentField++++++" + parentField);

                        if (parentField == null) {
                            System.out.println(dtoName + " не подошло!");
                            continue;
                        }

//                            System.out.println(repoIndex + " childDTOMethodParentDTOList.size()" + childDTOMethodParentDTOList.size());
//                            System.out.println(methodInd + "childDTOMethodParentDTOList.get(repoIndex).size()" + childDTOMethodParentDTOList.get(repoIndex).size());

                        childDTOMethodParentDTOList.get(repoIndex).get(methodInd).add(parentDTOInd);
                        childDTOMethodParentFieldDTOList.get(repoIndex).get(methodInd).add(parentField);

                        childDTOMethodParentMethodRequestList.get(repoIndex).get(methodInd).add(fieldIndexFromDto);
                        break;
                    }
                }
            } else {
                fieldsLower[fieldIndexFromDto] = fieldName;
            }
        }

        parameters.get(repoIndex).add(fieldsLower);

        parameterDTOFieldInds.get(repoIndex).add(paramFieldIndex);

        mapRepos.get(repoIndex).add(new ConcurrentHashMap<>());
        //сетим порядковый номер метода по названию
        methodOrders.get(repoIndex).put(findMethodName, methodInd);

        if (!dependsOnParentField) {
            DTOParamFreeFieldIndex.get(repoIndex).add(Arrays.asList(paramFieldIndex));
        } else {
            List<Integer> cdc = new ArrayList<>();
            for (Integer r : paramFieldIndex) {
                if (r != null) {
                    cdc.add(r);
                }
            }

            DTOParamFreeFieldIndex.get(repoIndex).add(cdc);
        }
        DTOMethodDependentToIndependent.get(repoIndex).add(null);


//            System.out.println(" dtoNames.get("+repoIndex+") " + dtoNames.get(repoIndex)
//                    + " paramFieldIndex " + Arrays.toString(paramFieldIndex) + " ");

        DTOParamFieldIndex.get(repoIndex).add(paramFieldIndex);



        if (method.getReturnType().isAssignableFrom(List.class)) {
            collectionType.get(repoIndex).add(1);
        } else if (method.getReturnType().isAssignableFrom(Set.class)) {
            collectionType.get(repoIndex).add(2);
        } else {
            collectionType.get(repoIndex).add(0);
        }

//        System.out.println("862 collectionType.get("+repoIndex+")[" + (collectionType.get(repoIndex).size() -1) + "] "
//        + collectionType.get(repoIndex).get(collectionType.get(repoIndex).size() - 1)
//                + " findMethodName " + findMethodName);
    }

    //@Around("@annotation(CacheRequest)")
    //@Around("execution(* ru.skidoz.aop.repo.*.findBy*(..))")
    /*public Object processFind(ProceedingJoinPoint joinPoint) throws Exception {

        System.out.println(joinPoint.getThis().getClass().getSimpleName() + " processFind " + joinPoint.getSignature
        ().getName() );

        var repoFile = joinPoint.getThis().getClass().getSimpleName();
        var repoIndex = repos.get(repoFile.substring(0, repoFile.length() - 20));
        var methodName = joinPoint.getSignature().getName();
        var args = joinPoint.getArgs();

        return processFind(repoIndex, methodName, args);
    }*/

    private Object processFindAll(Integer repoIndex) {

        return idMap.get(repoIndex).values().stream().toList();
    }


    private DTO processFindById(Integer repoIndex, int id) {

            DTO dto = idMap.get(repoIndex).get(id);
            if (dto != null) {
                return dto;
            } else {

                System.out.println("******************");
                System.out.println(jpaRepositories.get(repoIndex));
                System.out.println(jpaRepositories.get(repoIndex).findById(id));


                Object entity = jpaRepositories.get(repoIndex).findById(id).orElse(null);
                if (entity != null) {
                    DTO foundDTO = (DTO) mapRepositories.get(repoIndex).toDto(entity);
                    idMap.get(repoIndex).put(id, foundDTO);

                    return foundDTO;
                }
                return null;
            }
    }

    private Object deleteById(Integer repoIndex, int id) {

        DTO dto = idMap.get(repoIndex).get(id);
        if (dto != null) {
            return dto;
        } else {
            return jpaRepositories.get(repoIndex).findById(id).orElse(null);
        }
    }

    private Object processFind(Integer repoIndex, String methodName, Object[] args)
            throws Exception {
        /*int parentIndex = -1;
        if (methodName.equals("findByLevel_Id")) {
            parentIndex = repos.get("LevelCacheRepository");
        } else if (methodName.equals("findByUser_Id")) {
            parentIndex = repos.get("UserCacheRepository");
        }
        if (parentIndex > -1) {
            Set<Integer> ids = mapDependant.get(parentIndex).get(repoIndex).get((int) args[0]);
            ArrayList<Object> objects = new ArrayList<>();
            for (Integer id : ids) {
                objects.add(idMap.get(repoIndex).get(id));
            }
            return objects;
        }*/
        int methodOrder = methodOrders.get(repoIndex).get(methodName);

        System.out.println(dtoNames.get(repoIndex) + " _+_+_methodName "
                + methodName + " methodOrder--- " + methodOrder);


        var parentDTOs = childDTOMethodParentMethodDTOList.get(repoIndex).get(methodOrder);

        if (!parentDTOs.isEmpty()) {
            List<Integer> idss = new ArrayList<>();

            for (int i = 0; i < parentDTOs.size(); i++) {

                var parentRepoInd = parentDTOs.get(i);
                var parentMethodInd = childDTOMethodParentMethodMethodList.get(repoIndex)
                        .get(methodOrder).get(i);

                var requestParams = childDTOMethodParentMethodRequestParamsList.get(repoIndex)
                        .get(methodOrder).get(i);

                int type = collectionType.get(parentRepoInd).get(parentMethodInd);

                Set<Integer> childIds = null;
                //List<Integer> parentResult = null;
                //int parentId = 0;
                if (parentMethodInd == 0/* && args.length == 1*/) {
                    int parentId = (int) args[0];

                    if (type == 0) {
                        childIds = mapDependant
                                .get(parentRepoInd)
                                .get(repoIndex)
                                .get(parentId);

                        if (i == 0) {
                            idss.addAll(childIds);
                        }
                    } else {
                        childIds = mapDependant
                                .get(parentRepoInd)
                                .get(repoIndex)
                                .get(parentId);
                    }
                } else {
                    StringJoiner parentJoiner = new StringJoiner("_");
                    for (var requestParam : requestParams) {
                        parentJoiner.add(args[requestParam].toString());
                    }
                    var parentKey = parentJoiner.toString();

                    var parentResult = mapRepos.get(parentRepoInd).get(parentMethodInd).get(parentKey);

                    if (type == 0) {
                        var parentId = ((DTO) parentResult).getId();


                        ///mapDependant - не верный выбор
                        childIds = mapDependant.get(parentRepoInd).get(repoIndex).get(parentId);

//                        System.out.println("childIds+++++" + childIds);

                        if (i == 0) {
                            idss.addAll(childIds);
                        }
                    } else {
                        childIds = ((HashMap<Integer, DTO>) parentResult).values().stream()
                                .map(DTO::getId)
                                .map(parentsId -> mapDependant
                                        .get(parentRepoInd)
                                        .get(repoIndex)
                                        .get(parentsId))
                                .flatMap(Collection::stream)
                                .collect(Collectors.toSet());
                    }
                }
                /*if (type == 0) {
                    //var parentId = ((DTO) parentResult).getId();
                    childIds = mapDependant.get(parentRepoInd).get(repoIndex).get(parentId);
                    System.out.println("childIds+++++" + childIds);
                    if (i == 0) {
                        idss.addAll(childIds);
                    }
                } else {
                    childIds = ((HashMap<Integer, DTO>) parentResult).values().stream()
                            .map(DTO::getId)
                            .map(parentsId -> mapDependant
                                    .get(parentRepoInd)
                                    .get(repoIndex)
                                    .get(parentsId))
                            .flatMap(Collection::stream)
                            .collect(Collectors.toSet());
                }*/
//                System.out.println("childIds+++ " + childIds);

                if (childIds != null) {
                    if (i == 0) {
                        idss.addAll(childIds);
                    } else {
                        idss.retainAll(childIds);
                    }
                }
            }

            int methodIndex = DTOMethodDependentToIndependent.get(repoIndex).get(methodOrder);
            List<Integer> childParamIndexList = null;

            childParamIndexList = DTOParamFreeFieldIndex.get(repoIndex).get(methodIndex);

            StringJoiner joiner = new StringJoiner("_");
            for (Integer i : childParamIndexList) {
                joiner.add(args[i].toString());
            }
            var childKey = joiner.toString();

            Object childObject = mapRepos.get(repoIndex).get(methodIndex).get(childKey);
            if (childObject == null) {


                Method method = jpaFindMethods.get(repoIndex).get(methodOrder);
                System.out.println(" parentRepoInd "
                        + " repoIndex " +  dtoNames.get(repoIndex)
                        + " methodOrder " + methodOrder
                        + " methodName-" + methodName);
                System.out.println("*-+ " + jpaFindMethods.get(repoIndex));


                if (method != null) {
                    AbstractEntity entity =
                            (AbstractEntity) method.invoke(jpaRepositories.get(repoIndex), args);
                    if (entity == null) {
                        return null;
                    }
                    childObject = mapRepositories.get(repoIndex).toDto(entity);
                } else {
                    throw new Exception("type == 000");
                }
            }

            int type = collectionType.get(repoIndex).get(methodIndex);
            int returnType = collectionType.get(repoIndex).get(methodOrder);

            if (returnType == 0) {
                if (type == 0) {
                    if (idss.contains(((DTO) childObject).getId())) {
                        return childObject;
                    }
                } else {
                    for (var e : ((HashMap<Integer, DTO>) childObject).values()) {
                        if (idss.contains(e.getId())) {
                            return e;
                        }
                    }
                    return null;
                }
            } else if (returnType == 1) {
//                Collection<DTO> values = ((HashMap<Integer, DTO>) childObject).values();
//                int valueSize = values.size();
//                int idssSize = idss.size();

                return ((HashMap<Integer, DTO>) childObject).values().stream()
                        .filter(e -> idss.contains(e.getId()))
                        .toList();
            } else if (returnType == 2) {
                return ((HashMap<Integer, DTO>) childObject).values().stream()
                        .filter(e -> idss.contains(e.getId()))
                        .collect(Collectors.toSet());
            }
        }

        StringJoiner joiner = new StringJoiner("_");
        for (Object arg : args) {
            joiner.add(arg.toString());
        }

        var key = joiner.toString();

        //взяли из methods порядковый номер метода
        var result = mapRepos.get(repoIndex).get(methodOrder).get(key);

        if (result == null) {
            int type = collectionType.get(repoIndex).get(methodOrder);

            if (type == 0) {
                Method method = jpaFindMethods.get(repoIndex).get(methodOrder);

                if (method != null) {
                    AbstractEntity entity =
                            (AbstractEntity) method.invoke(jpaRepositories.get(repoIndex), args);
                    if (entity == null) {
                        return null;
                    }
                    return mapRepositories.get(repoIndex).toDto(entity);
                } else {
                    throw new Exception("type == 0");
                }
                //return save((DTO) joinPoint.proceed(), repoIndex);
            } else if (type == 1) {
                throw new Exception("type == 1");
//                var dtos = (List<DTO>) joinPoint.proceed();
//                for (var dto : dtos) {
//                    save(dto, repoIndex);
//                }
//                return dtos;
            } else if (type == 2) {
                throw new Exception("type == 2");
//                var dtos = (Set<DTO>) joinPoint.proceed();
//                for (var dto : dtos) {
//                    save(dto, repoIndex);
//                }
//                return dtos;
            }
            return null;
        } else {
            //EntityMapper mapper = (EntityMapper) context.getBean("PersonEntityMapperImpl");
            int type = collectionType.get(repoIndex).get(methodOrder);

            if (type == 0) {
                return result;
            } else if (type == 1) {
                return new ArrayList<DTO>(((HashMap) result).values());
            } else if (type == 2) {
                return new HashSet<DTO>(((HashMap) result).values());
            }
            return null;
            //mapper.toEntity(result);
        }
    }

    //@Around("execution(* ru.skidoz.aop.repo.*.save(..))")
    /*public Object processSave(ProceedingJoinPoint joinPoint) {

        var repoFile = joinPoint.getThis().getClass().getSimpleName();

        DTO dto = (DTO) joinPoint.getArgs()[0];
        var repoIndex = repos.get(repoFile.substring(0, repoFile.length() - 20));

        try {
            return save(dto, repoIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    private DTO cache(DTO dto, Integer repoIndex)
            throws IllegalAccessException, NoSuchFieldException {
        return saveToCache(dto, repoIndex, true);
    }

    private DTO save(DTO dto, Integer repoIndex)
            throws IllegalAccessException, NoSuchFieldException {

        int id = dto.getId();
        idMap.get(repoIndex).put(id, dto);

        if (id < 0) {
            idNewMap.get(repoIndex).put(id, dto);
        }

        return saveToCache(dto, repoIndex, false);
    }

    private DTO saveToCache(DTO dto, Integer repoIndex, boolean cache)
            throws IllegalAccessException, NoSuchFieldException {

        //если новый обьект
        if (dto.getFieldValues() == null) {


//            System.out.println("save new dto " + dto);

            var fieldValues = new ArrayList<>();
            Field idField;
            if (dto.getClass().getSuperclass().getSuperclass().getSimpleName().equals("DTO")) {
                idField = dto.getClass().getSuperclass().getSuperclass().getDeclaredField("id");
            } else {
                idField = dto.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("id");
            }

            idField.setAccessible(true);
            Integer idValue = (Integer) idField.get(dto);
            idMap.get(repoIndex).put(idValue, dto);


            fieldValues.add(dto.getId());

            int fieldIndex = 0;
            for (var fieldName : dtoFieldNames.get(repoIndex)) {

                var field = dto.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                var fieldValue = field.get(dto);
                fieldValues.add(fieldValue);

                var parentDT0Ind = childDTOFieldParentDTOIndex.get(repoIndex).get(fieldIndex);

//                System.out.println("@_+_@--child " + dtoNames.get(repoIndex) + " fieldName " + fieldName
//                        + " parentDT0Ind  " + parentDT0Ind + " fieldValue " + fieldValue + "");



                if (parentDT0Ind != null && fieldValue != null) {

//                    System.out.println(
//                            "parent " + dtoNames.get(parentDT0Ind) + " " +
//                            " child  " + dtoNames.get(repoIndex) + " fieldName " + fieldName
//                            + " fieldValue--" + fieldValue);

                    int parentDTOId = (Integer) fieldValue;

                    var set = mapDependant.get(parentDT0Ind).get(repoIndex).get(parentDTOId);

                    if (set == null) {
                        set = new HashSet<>();
                        mapDependant.get(parentDT0Ind).get(repoIndex).put((Integer) fieldValue, set);
                    }
                    set.add(dto.getId());

                    final DTO parentDTO = idMap.get(parentDT0Ind).get(parentDTOId);

                    addChildToParent(parentDTO, listDtos[parentDT0Ind][repoIndex], dto);
                }
                fieldIndex++;
            }

            dto.setFieldValues(fieldValues);
            //var dtoFields = dtoFieldNames.get(repoIndex);

            var keys = new ArrayList<String>();
            dtoKeys.get(repoIndex).put(dto.getId(), keys);


            for (int i = 0; i < parameters.get(repoIndex).size(); i++) {

                var parameter = parameters.get(repoIndex).get(i);
                var submap = mapRepos.get(repoIndex).get(i);

                List<String> newKeyArr = new ArrayList<>();//String[parameter.length];
                for (String param : parameter) {
                    if (param != null && !param.equals("id")) {

                        var field = dto.getClass().getDeclaredField(param);
                        field.setAccessible(true);

                        var newFieldValue = field.get(dto);
                        if (newFieldValue == null) {
                            newKeyArr.add(null);
                        } else {
                            newKeyArr.add(newFieldValue.toString());
                        }
                    }
                }

                String newKey = String.join("_", newKeyArr);

                int type = collectionType.get(repoIndex).get(i);
                if (type == 0) {
                    submap.put(newKey, dto);

//                    System.out.println("A mapRepos.get("+repoIndex+").get("+i+")["+newKey+"]" + dto);
                } else {

                    var markMap = (HashMap<Integer, DTO>) submap.get(newKey);
                    if (markMap == null) {
                        markMap = new HashMap<>();
                        submap.put(newKey, markMap);
                    }
                    markMap.put(dto.getMark(), dto);

//                    System.out.println("B mapRepos.get("+repoIndex+").get("+i+")["+newKey+"]" + dto);
                }
                keys.add(newKey);
            }
        } else {//если изменяем старый обьект
            var dtoFields = dtoFieldNames.get(repoIndex);
            int fieldSize = dtoFields.length;

            boolean[] fieldsChanged = new boolean[fieldSize];
            Object[] previousFields = new Object[fieldSize];

            int newFieldIdValue = dto.getId();

            int oldId = (int) dto.getFieldValues().get(0);

            if (oldId != newFieldIdValue) {

                dtoKeys.get(repoIndex).put(
                        newFieldIdValue,
                        dtoKeys.get(repoIndex).remove(oldId));

                var chDTO = mapDependant.get(repoIndex);

                for (int childInd = 0; childInd < chDTO.size(); childInd++) {

                    Map<Integer, Set<Integer>> parentChildren = chDTO.get(childInd);

                    //все id дочерних -
                    var dependentIds = parentChildren.get(oldId);
                    parentChildren.put(newFieldIdValue, dependentIds);

                    if (dependentIds == null || dependentIds.size() == 0) {
                        continue;
                    }

                    int childOrder = parentDTOChildDTOIndex.get(repoIndex).indexOf(childInd);
                    int fieldIndex = parentDTOChildDTOFieldIndex.get(repoIndex).get(childOrder);
                    String fieldName = dtoFieldNames.get(childInd)[fieldIndex];

                    for (var dependentId : dependentIds) {
                        DTO childDto = idMap.get(childInd).get(dependentId);
                        Field field = childDto.getClass().getDeclaredField(fieldName);
                        field.setAccessible(true);
                        field.set(childDto, newFieldIdValue);
                    }

                    List<Integer> methodList = parentDTOChildMethodIndex.get(repoIndex).get(childInd);
                    List<Integer> paramList = parentDTOChildParamIndex.get(repoIndex).get(childInd);
                    var paramField = parameterDTOFieldInds.get(childInd);
                    var childMap = mapRepos.get(childInd);

//                    System.out.println(dtoNames.get(childInd) + " childMap***+-+ " + childMap);

                    for (int methodI = 0; methodI < methodList.size(); methodI++) {

                        int methodInd = methodList.get(methodI);
                        int paramInd = paramList.get(methodI);

                        var parameter = paramField.get(methodInd);
                        //значения по методу поиска
                        var childSubmap = childMap.get(methodInd);

                        String[] newKeyArr = new String[parameter.length];
                        String[] oldKeyArr = new String[parameter.length];

                        for (var dependentId : dependentIds) {

                            DTO childDto = idMap.get(childInd).get(dependentId);

                            //метод поиска find
                            for (int j = 0; j < parameter.length; j++) {
                                //порядок в мапе полей значений dto
                                int fieldInd = parameter[j] + 1;
                                //индекс измененного поля
                                oldKeyArr[j] = childDto.getFieldValues().get(fieldInd).toString();
                                if (j == paramInd) {
                                    newKeyArr[j] = Integer.valueOf(newFieldIdValue).toString();
                                } else {
                                    newKeyArr[j] = oldKeyArr[j];
                                }
                            }

                            ((HashMap<Integer, DTO>) childSubmap.get(String.join("_", oldKeyArr))).remove(childDto.getMark());

                            var newKey = String.join("_", newKeyArr);

                            var byKey = (HashMap<Integer, DTO>) childSubmap.get(newKey);
                            if (byKey == null) {
                                byKey = new HashMap<>();
                                childSubmap.put(newKey, byKey);
                            }
                            byKey.put(childDto.getMark(), childDto);

//                            System.out.println("C mapRepos.get("+childInd+").get("+methodInd+")["+newKey+"]" + childDto);

                        }
                    }

//                    System.out.println(dtoNames.get(childInd) + " childMap+-+-+ " + childMap);
                }

                dto.getFieldValues().set(0, newFieldIdValue);
            }

            for (int fieldIndex = 0; fieldIndex < fieldSize; fieldIndex++) {

                var fieldName = dtoFields[fieldIndex];

                var field = dto.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                var newFieldValue = field.get(dto);
                var oldValue = dto.getFieldValues().get(fieldIndex + 1);

//                System.out.println(dtoNames.get(repoIndex) + " @@_++_@ fieldName " + fieldName + " oldValue "
//                        + oldValue + " newFieldValue " + newFieldValue);

                //если значение поля изменилось
                if (newFieldValue != null && !newFieldValue.equals(oldValue)
                        || (newFieldValue == null && oldValue != null)) {
                    //идут в паре
                    previousFields[fieldIndex] = oldValue;
                    fieldsChanged[fieldIndex] = true;

                    //обновляем значение поля в dto
                    dto.getFieldValues().set(fieldIndex + 1, newFieldValue);
                    //если поменяли не id
                    //если поменяли в дочернем объекте поле - перекидываем его id в куче дочерних элементов из его
                    // предыдущего родителя в кучу нового

                    var parentDT0 = childDTOFieldParentDTOIndex.get(repoIndex).get(fieldIndex);

//                    System.out.println("update parentDT0 " + parentDT0
//                            + " child " + dtoNames.get(repoIndex)
//                            + " oldValue " + oldValue + " newFieldValue " + newFieldValue);


                    if (parentDT0 != null) {

//                        System.out.println(" mapDependant.get(parentDT0).get(repoIndex).get((Integer) oldValue) "
//                                + mapDependant.get(parentDT0).get(repoIndex).get((Integer) oldValue));
//                        System.out.println("mapDependant.get(parentDT0).get(repoIndex).get((Integer) newFieldValue) "
//                                + mapDependant.get(parentDT0).get(repoIndex).get((Integer) newFieldValue));



//                        System.out.println("parent*_-+-+" + dtoNames.get(parentDT0));


                        Set<Integer> oldSet = mapDependant.get(parentDT0).get(repoIndex).get((Integer) oldValue);
                        if (oldSet != null) {
                            mapDependant.get(parentDT0).get(repoIndex).get((Integer) oldValue).remove(dto.getId());
                        }

                        Set<Integer> newSet = mapDependant.get(parentDT0).get(repoIndex).get((Integer) newFieldValue);
                        if (newSet == null) {
                            newSet = new HashSet<>();
                            mapDependant.get(parentDT0).get(repoIndex).put((Integer) newFieldValue, newSet);
                        }
                        newSet.add(dto.getId());
                    }
                }
                //fieldIndex++;
            }

            var keys = dtoKeys.get(repoIndex).get(dto.getId());

            //репозиторий - метод поиска find
            for (int methodInd = 0; methodInd < parameterDTOFieldInds.get(repoIndex).size(); methodInd++) {
                //метод поиска find
                var parameter = parameterDTOFieldInds.get(repoIndex).get(methodInd);
                //значения по методу поиска
                var submap = mapRepos.get(repoIndex).get(methodInd);

                //String[] newKeyArr = new String[parameter.length];
                List<String> newKeyArr = new ArrayList<>();
                //String[] oldKeyArr = new String[parameter.length];
                List<String> oldKeyArr = new ArrayList<>();
                //метод поиска find
                for (int j = 0; j < parameter.length; j++) {
                    //порядок в мапе полей значений dto

                    if (parameter[j] == null) {
                        //если поле - относится к полю родитеского класса - оно точно не меняется сейчас
                        continue;
                    }
                    int fieldInd = parameter[j];
                    //индекс измененного поля

                    //newKeyArr[j] = dto.getFieldValues().get(fieldInd + 1).toString();

                    Object ind = dto.getFieldValues().get(fieldInd + 1);
                    String indStr = null;
                    if (ind != null) {
                        indStr = ind.toString();
                    }

                    newKeyArr.add(indStr);

                    if (fieldsChanged[fieldInd]) {
                        //changedFieldNames идет параллельно с previousFields
                        //oldKeyArr[j] = previousFields[fieldInd].toString();

                        if (previousFields[fieldInd] == null) {
                            oldKeyArr.add(null);
                        } else {
                            oldKeyArr.add(previousFields[fieldInd].toString());
                        }
                    } else {
                        //oldKeyArr[j] = indStr;
                        oldKeyArr.add(indStr);
                    }
                }

                String newKey = String.join("_", newKeyArr);
                keys.set(methodInd, newKey);

//                System.out.println("@!@dtoNames.get(" + repoIndex+ ") " + dtoNames.get(repoIndex) + " ----"
//                + String.join("_", oldKeyArr));
//                System.out.println(Arrays.toString(parameters.get(repoIndex).get(methodInd)));


                int type = collectionType.get(repoIndex).get(methodInd);
                if (type == 0) {
                    submap.remove(String.join("_", oldKeyArr), dto);
                    submap.put(newKey, dto);

//                    System.out.println("E mapRepos.get("+repoIndex+").get("+methodInd+")["+newKey+"]" + dto);

                } else {
                    var o = (Map<Integer, DTO>) submap.get(String.join("_", oldKeyArr));

                    if (o != null) {
                        o.remove(dto.getMark());
                    }

                    var n = (Map<Integer, DTO>) submap.get(newKey);
                    if (n == null) {
                        n = new HashMap<>();
                        n.put(dto.getMark(), dto);
                        submap.put(newKey, n);


//                        System.out.println("F mapRepos.get("+repoIndex+").get("+methodInd+")["+newKey+"]" + dto);

                    } else {
                        n.put(dto.getMark(), dto);
                    }
                }
            }
        }

        final String[] repoFieldCollectionNames = dtoFieldCollectionNames.get(repoIndex);

        //System.out.println("dtoFieldCollectionNames " + repoIndex + " " + Arrays.toString(repoFieldCollectionNames));

        for (int i = 0; i < repoFieldCollectionNames.length; i++) {

            var field = dto.getClass().getDeclaredField(repoFieldCollectionNames[i]);
            field.setAccessible(true);

            List<DTO> fieldValues = (List<DTO>) field.get(dto);
            int childDTOIndex = dtoFieldCollectionOrder.get(repoIndex)[i];

            //System.out.println(fieldValues + " @@@++++ " + childDTOIndex);

            for (DTO fieldValue : fieldValues) {
                if (cache) {
                    saveToCache(fieldValue, childDTOIndex, true);
                } else {
                    save(fieldValue, childDTOIndex);
                }
            }
        }


//        int u = 0;
//        for (var s : mapRepos.get(repoIndex)) {
//            System.out.println(dtoNames.get(repoIndex)
//                    + " Q parameters " + (parameters.get(repoIndex).size() > u
//                    ? Arrays.toString(parameters.get(repoIndex).get(u)) : null)
//                    + " methodInd " + u + " repoIndex " + repoIndex
//                    + " ++*-* " + u + " *** " + s);
//            u++;
//        }

        return dto;
    }

    private void addChildToParent(DTO parentDTO, String fieldName, DTO dto)
            throws IllegalAccessException, NoSuchFieldException {

        var field = parentDTO.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        var list = (List) field.get(parentDTO);
        list.add(dto);
    }

    private String firstLower(String s) {
        char c[] = s.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    private String firstUpper(String s) {
        char c[] = s.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    private void processParentDTO() {
        //мы ищем совпадения названий полей в DTO
        for (int parentDTOInd = 0; parentDTOInd < dtoNames.size(); parentDTOInd++) {
            for (int childDTOInd = 0; childDTOInd < dtoFieldNames.size(); childDTOInd++) {
                for (int childFieldInd = 0; childFieldInd < dtoFieldNames.get(childDTOInd).length; childFieldInd++) {

                    var childFieldName = dtoFieldNames.get(childDTOInd)[childFieldInd];

//                    System.out.println(" child " + dtoNames.get(childDTOInd)
//                            + " childFieldName " + childFieldName
//                            + " ----- parent " + dtoNames.get(parentDTOInd) + " " + childFieldName.startsWith(dtoNames.get(parentDTOInd)));

                    if (childFieldName.startsWith(dtoNames.get(parentDTOInd))) {

                        //остаток названия поля дочернего класса - должен называться как поле родителя
                        var childFieldNameRest = childFieldName.substring(dtoNames.get(parentDTOInd).length());

//                        System.out.println("parent " + dtoNames.get(parentDTOInd)
//                                + " child " + dtoNames.get(childDTOInd)
//                                + " childFieldNameRest----------" + childFieldNameRest +
//                        " childFieldName " + childFieldName);

                        //если правда называется так - значит дочерний - ищем индекс этого поля
                        if (childFieldNameRest.equals("Id")) {
                            parentDTOChildDTOIndex.get(parentDTOInd).add(childDTOInd);
                            parentDTOChildDTOFieldIndex.get(parentDTOInd).add(childFieldInd);

                            childDTOFieldParentDTOIndex.get(childDTOInd).set(childFieldInd, parentDTOInd);
                            //просто список методов, где используется поле
                            //                                 дочерний dto     зависимое поле
                            int size = DTOFieldMethodIndex.get(childDTOInd).get(childFieldInd).size();
                            for (int y = 0; y < size; y++) {
                                int childMethod = DTOFieldMethodIndex.get(childDTOInd).get(childFieldInd).get(y);
                                int childParam = DTOFieldParamIndex.get(childDTOInd).get(childFieldInd).get(y);
                                //а это список методов для дочернего dto, где используется ссылка на поле
                                parentDTOChildMethodIndex.get(parentDTOInd).get(childDTOInd)
                                        .add(childMethod);
                                //а это список параметров - парных для мтеодов для дочернего dto,
                                // где используется ссылка на поле
                                parentDTOChildParamIndex.get(parentDTOInd).get(childDTOInd)
                                        .add(childParam);
                            }
                        }
                    }
                }
            }
        }
    }

}
