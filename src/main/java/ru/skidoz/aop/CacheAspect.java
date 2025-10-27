package ru.skidoz.aop;

import static org.springframework.util.CollectionUtils.isEmpty;

import org.apache.commons.lang3.ArrayUtils;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import ru.skidoz.model.entity.AbstractEntity;
import jakarta.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
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

    public Map<String, Integer> repoOrders = new HashMap<>();

    public List<ConcurrentHashMap<Integer, DTO>> idMap = new ArrayList<>();

    public List<ConcurrentHashMap<Integer, DTO>> idUpdateMap = new ArrayList<>();

    public List<ConcurrentHashMap<Integer, DTO>> idNewMap = new ArrayList<>();

    public List<List<Integer>> idDeleteMap = new ArrayList<>();
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
    Map<Integer, Class<?>> dtoClasses = new HashMap<>();
    List<String[]> dtoFieldNames = new ArrayList<>();
    List<String[]> dtoFieldCollectionNames = new ArrayList<>();
    List<Integer[]> dtoFieldCollectionOrder = new ArrayList<>();
    List<Map<String, Integer>> dtoFieldsMap = new ArrayList<>();
    //List<List<Integer>> dtoFieldNames = new ArrayList<>();

//    List<List<Integer>> parentDTOChildDTOIndex = new ArrayList<>();
    List<List<Boolean>> parentDTOBlockDTOIndex = new ArrayList<>();
    List<List<Integer>> parentDTOBlockArrIndex = new ArrayList<>();

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
    List<List<List<Integer>>> childDTOMethodChildMethodSplitIndParentAttributeList = new ArrayList<>();
    List<List<List<Integer>>> childDTOMethodParentFieldDTOList = new ArrayList<>();

    List<List<List<Integer>>> childDTOMethodParentMethodMethodList = new ArrayList<>();
    List<List<List<List<Integer>>>> childDTOMethodParentMethodRequestParamsList = new ArrayList<>();
    List<List<List<Integer>>> childDTOMethodParentMethodDTOList = new ArrayList<>();
    //List<List<Integer[]>> DTOParamFieldIndex = new ArrayList<>();
    List<List<Method>> jpaFindMethods = new ArrayList<>();

    //надо придумать использование
    List<List<List<Integer>>> DTOParamFreeFieldIndex = new ArrayList<>();
    //
    List<List<Integer>> DTOMethodDependentToIndependent = new ArrayList<>();

    List<JpaRepository<?, Integer>> jpaRepositories = new ArrayList<>();
    List<EntityMapper> mapRepositories = new ArrayList<>();


    private ApplicationContext context;

    public CacheAspect(ApplicationContext context)
            throws NoSuchMethodException, NoSuchFieldException {
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

                    Integer dtoIndex = dtoNamePlaces.get(dtoName.substring(
                            0,
                            dtoName.length() - "Entity".length()));

                    if (dtoIndex != null) {

                        JpaRepository<?, Integer> myBean = (JpaRepository<?, Integer>) context
                                .getBean(firstLower(interf.getSimpleName()));

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

                System.out.println(dtoIndex + " *-*-@@-*-* " + interf.getSimpleName());

                if (dtoIndex != null) {
                    EntityMapper myBean = (EntityMapper) context
                            .getBean(firstLower(interf.getSimpleName() + "Impl"));

                    System.out.println(" *-*-@@ myBean " + myBean);

                    mapRepositories.set(dtoIndex, myBean);
                }
            }
        }
    }

    public void ee() throws NoSuchMethodException, NoSuchFieldException {
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
                    dtoClasses.put(i, clazz);
                    i++;

                    List<String> methodHashes = new ArrayList<>();
                    implJpaMethodNames.add(methodHashes);

                    for (Method declaredMethod : interf.getDeclaredMethods()) {

                        if (Arrays.stream(declaredMethod.getParameters())
                                .noneMatch(t -> t.getType().getSimpleName().equals("Object"))) {
                            String hash = declaredMethod.getName().replaceAll("_Id", "") + ":" +
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

                    Integer dtoIndex = dtoNamePlaces.get(dtoName.substring(
                            0,
                            dtoName.length() - "Entity".length()));


                    if (dtoIndex != null) {

                        for (Method declaredMethod : interf.getDeclaredMethods()) {
                            if (Arrays.stream(declaredMethod.getParameters())
                                    .noneMatch(t -> t.getType().getSimpleName().equals("Object"))) {

                                String methodName = declaredMethod.getName()
                                        .replaceAll("_Id", "")
                                        .replaceAll("Entity", "");

                                String hash = methodName + ":" +
                                        Arrays.stream(declaredMethod.getParameters())
                                                .map(t -> {
                                                    String simpleName = t.getType().getSimpleName();
                                                    if (simpleName.endsWith("Entity")) {
                                                        return simpleName.substring(
                                                                0,
                                                                simpleName.length()
                                                                        - "Entity".length());
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
                    public Object invoke(Object proxy, Method method, Object[] args)
                            throws Throwable {

                        var repoFile = interfaceClass.getSimpleName();

                        var repoIndex = repoOrders.get(repoFile);
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
                                if (methodName.startsWith("find")) {//findBy findAllBy
                                    try {
                                        readLock.lock();
                                        yield processFind(repoIndex, methodName, args);
                                    } finally {
                                        readLock.unlock();
                                    }
                                }
                                throw new IllegalArgumentException("Unsupported method: "
                                        + methodName
                                        + " "
                                        + Arrays.toString(args));
                            }
                        };
                    }
                }
        );
    }

    String[][] listDtos = null;
    List<Integer> dtoSuperclassFieldStart = new ArrayList<>();

    //@PostConstruct
    public void init(
            List<Class<? extends JpaRepositoryTest>> subTypes,
            List<Class<?>> dtoTypes,
            List<List<String>> implJpaMethodNames,
            List<String> typeNames,
            List<Map<String, Method>> jpaMethods
    ) throws NoSuchMethodException {

        Field[][] declaredFieldsDTO = new Field[subTypes.size()][];

        int repoIndex = 0;
        for (var typeName : typeNames) {

            repoOrders.put(typeName, repoIndex);

            mapRepos.add(new ArrayList<>());
            idMap.add(new ConcurrentHashMap<>(1024));
            idNewMap.add(new ConcurrentHashMap<>(1024));
            idUpdateMap.add(new ConcurrentHashMap<>(1024));
            idDeleteMap.add(new ArrayList<>());

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

            Field[] declaredFields = ArrayUtils.addAll(
                    dtoTypes.get(repoIndex).getDeclaredFields(),
                    dtoTypes.get(repoIndex).getSuperclass().getDeclaredFields());
            declaredFieldsDTO[repoIndex] = declaredFields;

            dtoSuperclassFieldStart.add(dtoTypes.get(repoIndex).getDeclaredFields().length);

            dtoNames.add(firstLower(dtoTypes.get(repoIndex).getSimpleName()));

            List<String> a = new ArrayList<>();

            dtoFieldsMap.get(repoIndex).put("id", 0);
            childDTOFieldParentDTO.add(null);
            childDTOFieldParentFieldDTO.add(null);
            childDTOFieldParentField.add(null);

            int i = 0;
            for (int declaredFieldInd = 0; declaredFieldInd < declaredFields.length;
                 declaredFieldInd++) {
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

//            parentDTOChildDTOIndex.add(parentDTOFieldChildDTO);
            parentDTOChildDTOFieldIndex.add(parentDTOFieldChildField);

            childDTOFieldParentDTOIndex.add(childDTOFieldParentDTO);
            childDTOFieldParentFieldDTOIndex.add(childDTOFieldParentDTO);
            childDTOFieldParentFieldIndex.add(childDTOFieldParentDTO);

            //для id
            mapRepos.get(repoIndex).add(new ConcurrentHashMap<>(1024));

            repoIndex++;
        }
        //processParentInit();

        listDtos = new String[dtoNames.size()][dtoNames.size()];

        repoIndex = 0;
        for (var subtype : subTypes) {


            var blockList = new ArrayList<Boolean>();
            for (int i = 0; i < dtoNames.size(); i++) {
                blockList.add(false);
            }
            blockList.set(repoIndex, true);
            parentDTOBlockDTOIndex.add(blockList);
            var blockArr = new ArrayList<Integer>();
            blockArr.add(repoIndex);
            parentDTOBlockArrIndex.add(blockArr);


            //DTOParamFieldIndex.add(new ArrayList<>());
            DTOParamFreeFieldIndex.add(new ArrayList<>());
            DTOMethodDependentToIndependent.add(new ArrayList<>());

            childDTOMethodParentDTOList.add(new ArrayList<>());
            childDTOMethodParentFieldDTOList.add(new ArrayList<>());
            childDTOMethodChildMethodSplitIndParentAttributeList.add(new ArrayList<>());

            childDTOMethodParentMethodMethodList.add(new ArrayList<>());

            childDTOMethodParentMethodRequestParamsList.add(new ArrayList<>());

            childDTOMethodParentMethodDTOList.add(new ArrayList<>());


            var declaredFields = declaredFieldsDTO[repoIndex];
            List<Method> implMethods = new ArrayList<>();
            List<String> hashes = new ArrayList<>();

            for (Method declaredMethod : subtype.getDeclaredMethods()) {

                String hash = declaredMethod.getName().replaceAll("_Id", "") + ":" +
                        Arrays.stream(declaredMethod.getParameters())
                                .map(t -> t.getType().getSimpleName())
                                .collect(Collectors.joining(","));

                //System.out.println(504 + " " + dtoNames.get(repoIndex) + " " + hash + " " + implJpaMethodNames.get(repoIndex));

                if (implJpaMethodNames.get(repoIndex).contains(hash)) {
                    implMethods.add(declaredMethod);
                    implJpaMethodNames.get(repoIndex).remove(hash);

                    hashes.add(hash);
                }
            }

            Method jpaMethod = jpaMethods.get(repoIndex).get("findById:Integer");

            Method cacheMethod = subtype.getMethod("findById", Object.class);

            processMethod("findById", 6, repoIndex, jpaMethod, 0, cacheMethod);

            //теперь анализируем репозитории и методы поиска
            int methodInd = 1;
            int hashInd = 0;
            for (var method : implMethods) {
                var findMethodName = method.getName();

                String hash = hashes.get(hashInd);

                jpaMethod = jpaMethods.get(repoIndex).get(hash);
                hashInd++;

//                System.out.println(dtoNames.get(repoIndex) +  " @@--* " + hash + " " + methodInd + " " + jpaMethod);
//                System.out.println(jpaMethods.get(repoIndex));

                if (findMethodName.equals("findById")
                        || !(findMethodName.startsWith("findBy") && findMethodName.length() > 6
                        || findMethodName.startsWith("findAllBy") && findMethodName.length() > 9)) {
                    continue;
                }

                if (findMethodName.startsWith("findBy") && findMethodName.length() > 6) {
                    processMethod(findMethodName, 6, repoIndex, jpaMethod, methodInd, method);
                } else if (findMethodName.startsWith("findAllBy") && findMethodName.length() > 9) {
                    processMethod(findMethodName, 9, repoIndex, jpaMethod, methodInd, method);
                }

                methodInd++;
            }

            var parameter = parameters.get(repoIndex);
            List<List<Integer>> parentFieldParamIndex = new ArrayList<>();
            List<List<Integer>> parentFieldFindMethodIndex = new ArrayList<>();

            List<String> fieldNames = new ArrayList<>();
            List<Integer> order = new ArrayList<>();


            for (int declaredFieldInd = 0; declaredFieldInd
                    < declaredFields.length; declaredFieldInd++) {

                if ("List".equals(declaredFields[declaredFieldInd].getType().getSimpleName())) {

                    Type genericFieldType = declaredFields[declaredFieldInd].getGenericType();

//                    System.out.println( "560 ***@ " + genericFieldType.getTypeName());

                    if (genericFieldType instanceof ParameterizedType aType) {

                        final String typeName = aType.getActualTypeArguments()[0].getTypeName();
                        final String simpleName = firstLower(
                                typeName.substring(typeName.lastIndexOf(".") + 1));

                        final int index = dtoNames.indexOf(simpleName);

//                        System.out.println(dtoNames.get(repoIndex) +" 570 simpleName " + simpleName + " typeName " + typeName + " index " + index);

                        if (index > -1) {
                            order.add(index);
                            fieldNames.add(declaredFields[declaredFieldInd].getName());

//                            System.out.println("575 " + dtoNames.get(repoIndex) + " " + dtoNames.get(index) + " " + declaredFields[declaredFieldInd].getName());

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
            for (int childMethodInd = 0; childMethodInd < childDTOMethodParentDTOList.get(childInd)
                    .size(); childMethodInd++) {

                //уровень метода в репозитории - список родительских dto
                var parentDTOInd = childDTOMethodParentDTOList
                        .get(childInd)
                        .get(childMethodInd);
                //уровень метода в репозитории - список родительских филдов - парно с dto
                var parentDTOFieldInd = childDTOMethodParentFieldDTOList
                        .get(childInd)
                        .get(childMethodInd);

                var childMethodSplitIndParentsAttr = childDTOMethodChildMethodSplitIndParentAttributeList
                        .get(childInd)
                        .get(childMethodInd);

                if (!isEmpty(parentDTOInd)) {
                    List<Integer> parentDTOList = new ArrayList<>();
                    List<List<Integer>> parentDTOFieldList = new ArrayList<>();
                    List<List<Integer>> childDTOSplitParamList = new ArrayList<>();

                    //формирование структуры parent [parent-dto][parent-field]
                    for (int k = 0; k < parentDTOInd.size(); k++) {

//                        System.out.println(dtoNames.get(childInd) + " ***-@- " + childMethodInd
//                                + " @@@ " + dtoNames.get(parentDTOInd.get(k)));

                        int parentInd = parentDTOInd.get(k);
                        int parentFieldInd = parentDTOFieldInd.get(k);

                        int childMethodSplitIndParentAttr = childMethodSplitIndParentsAttr.get(k);

                        var indexInListParent = parentDTOList.indexOf(parentDTOInd.get(k));
                        if (indexInListParent == -1) {
                            indexInListParent = parentDTOList.size();
                            parentDTOList.add(parentInd);
                            parentDTOFieldList.add(new ArrayList<>());
                            childDTOSplitParamList.add(new ArrayList<>());
                        }
                        parentDTOFieldList.get(indexInListParent).add(parentFieldInd);
                        childDTOSplitParamList
                                .get(indexInListParent)
                                .add(childMethodSplitIndParentAttr);
                    }

                    var childParamFieldInds = new ArrayList<Integer>();
                    var paramFieldIndex = parameterDTOFieldInds
                            .get(childInd)
                            .get(childMethodInd);//DTOParamFieldIndex

                    for (int f = 0; f < paramFieldIndex.length; f++) {
                        if (paramFieldIndex[f] != null) {
                            childParamFieldInds.add(paramFieldIndex[f]);
                        }
                    }

                    boolean noSuitableChildMethod = true;
                    for (int childMethodIndVariant = 0;
                         childMethodIndVariant < parameterDTOFieldInds.get(childInd).size();
                         childMethodIndVariant++) {

                        Integer[] params = parameterDTOFieldInds
                                .get(childInd)
                                .get(childMethodIndVariant);

                        if (params.length == childParamFieldInds.size()) {
                            boolean cont = true;
                            for (var param : params) {
                                if (!childParamFieldInds.contains(param)) {
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

                        Integer[] childFieldsArr = new Integer[childParamFieldInds.size()];
                        String[] fieldNames = new String[childParamFieldInds.size()];
                        //Integer[] childSplitIndArr = new Integer[childParamFieldInds.size()];

                        for (int u = 0; u < childParamFieldInds.size(); u++) {
                            var childField = childParamFieldInds.get(u);

                            childFieldsArr[u] = childField;

                            //childSplitIndArr[u] = u;

                            var fieldName = dtoFieldNames.get(childInd)[childField];
                            fieldNames[u] = fieldName;
                        }

                        int newMethodIndex = parameters.get(childInd).size();


                        System.out.println("751 parameterDTOFieldInds --- " + dtoNames.get(childInd) + " " + Arrays.toString(childFieldsArr));


                        parameterDTOFieldInds.get(childInd).add(childFieldsArr);
                        parameters.get(childInd).add(fieldNames);


                        mapRepos.get(childInd).add(new ConcurrentHashMap<>());
                        collectionType.get(childInd).add(1);

                        DTOMethodDependentToIndependent
                                .get(childInd)
                                .set(childMethodInd, newMethodIndex);

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
                        List<Integer> childParamIndParentAttr = childDTOSplitParamList.get(k);

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
                                    childDTOMethodParentMethodDTOList
                                            .get(childInd)
                                            .get(childMethodInd)
                                            .add(parentInd);

                                    childDTOMethodParentMethodMethodList
                                            .get(childInd)
                                            .get(childMethodInd)
                                            .add(m);

                                    childDTOMethodParentMethodRequestParamsList
                                            .get(childInd)
                                            .get(childMethodInd)
                                            .add(childParamIndParentAttr);

                                    noSuitableParentMethod = false;
                                }
                            }
                        }

                        if (noSuitableParentMethod) {
                            Integer[] parentFieldsArr = new Integer[parentFields.size()];
                            String[] parentFieldNames = new String[parentFields.size()];
                            //Integer[] childFieldsArr = new Integer[childParamFieldInds.size()];

                            for (int u = 0; u < parentFields.size(); u++) {
                                var parentField = parentFields.get(u);

                                parentFieldsArr[u] = parentField;

                                var parentFieldName = dtoFieldNames.get(parentInd)[parentField];
                                parentFieldNames[u] = parentFieldName;
                            }


                            System.out.println("829 parameterDTOFieldInds --- " + dtoNames.get(parentInd) + " " + Arrays.toString(parentFieldsArr));

                            parameterDTOFieldInds.get(parentInd).add(parentFieldsArr);

                            parameters.get(parentInd).add(parentFieldNames);
                            mapRepos.get(parentInd).add(new ConcurrentHashMap<>());
                            collectionType.get(parentInd).add(1);

                            StringJoiner childJoiner = new StringJoiner("And");
                            for (var requestParam : parentFieldsArr) {
                                childJoiner.add(requestParam.toString());
                            }
                            var findMethodName = "findBy" + childJoiner.toString();
                            methodOrders
                                    .get(parentInd)
                                    .put(findMethodName, parameters.get(childInd).size());

                            childDTOMethodParentMethodDTOList.get(childInd).get(childMethodInd)
                                    .add(parentInd);

                            childDTOMethodParentMethodMethodList.get(childInd).get(childMethodInd)
                                    .add(parameterDTOFieldInds.get(parentInd).size() - 1);

                            childDTOMethodParentMethodRequestParamsList
                                    .get(childInd)
                                    .get(childMethodInd)
                                    .add(childParamIndParentAttr);
                        }
                    }
                }
            }
        }
        processParentDTO();
    }


    private void processMethod(
            String findMethodName,
            int beginIndex,
            int repoIndex,
            Method jpaMethod,
            int methodInd,
            Method method) {
        childDTOMethodParentDTOList.get(repoIndex).add(new ArrayList<>());
        childDTOMethodParentFieldDTOList.get(repoIndex).add(new ArrayList<>());
        childDTOMethodChildMethodSplitIndParentAttributeList.get(repoIndex).add(new ArrayList<>());
        childDTOMethodParentMethodMethodList.get(repoIndex).add(new ArrayList<>());
        childDTOMethodParentMethodRequestParamsList.get(repoIndex).add(new ArrayList<>());
        childDTOMethodParentMethodDTOList.get(repoIndex).add(new ArrayList<>());

        String[] findMethodSplitArr = findMethodName.substring(beginIndex).split("And");
        String[] fieldsLower = new String[findMethodSplitArr.length];
        Integer[] paramFieldIndex = new Integer[findMethodSplitArr.length];
        List<Integer> paramFieldIndexDependantFree = new ArrayList<>();


//        System.out.println("@jpaFindMethods " + dtoNames.get(repoIndex)
//                + " findMethodName " + findMethodName + " " + jpaFindMethods.get(repoIndex).size() + " " + jpaMethod);

        jpaFindMethods.get(repoIndex).add(jpaMethod);


        boolean dependsOnParentField = false;
        List<Integer> paramFieldIndexFilled = new ArrayList<>();

        for (int splitInd = 0; splitInd < findMethodSplitArr.length; splitInd++) {

            String paramSplitName = firstLower(findMethodSplitArr[splitInd]);
            // я ищу в этом классе
            paramFieldIndex[splitInd] = dtoFieldsMap.get(repoIndex).get(paramSplitName);


            if (paramFieldIndex[splitInd] == null) {
                if (paramSplitName.endsWith("_Id")) {

                    String s = paramSplitName.substring(0, paramSplitName.length() - 3);

                    System.out.println(903 + "@@@ ** " + s);

                    var paramFieldInd = dtoFieldsMap.get(repoIndex).get(s);
                    if (paramFieldInd == null) {

                        paramFieldInd = dtoFieldsMap.get(repoIndex).get(s + "Id");
                        if (paramFieldInd != null) {
                            paramFieldIndex[splitInd] = paramFieldInd;
                            paramSplitName = s + "Id";
                        }
                    } else {
                        paramFieldIndex[splitInd] = paramFieldInd;
                        paramSplitName = s;
                    }
                } else if (paramSplitName.endsWith("Id")) {

                    String s = paramSplitName.substring(0, paramSplitName.length() - 2);

                    System.out.println(921 + "@@@ ** " + s);

                    var paramFieldInd = dtoFieldsMap.get(repoIndex).get(s);
                    if (paramFieldInd == null) {

                        paramFieldInd = dtoFieldsMap.get(repoIndex).get(s + "Id");
                        if (paramFieldInd != null) {
                            paramFieldIndex[splitInd] = paramFieldInd;
                            paramSplitName = s + "Id";
                        }
                    } else {
                        paramFieldIndex[splitInd] = paramFieldInd;
                        paramSplitName = s;
                    }
                }
            }

            System.out.println(dtoNames.get(repoIndex) + " -*-*-----* "+ findMethodName + " param " + paramSplitName + "  " + paramFieldIndex[splitInd]);

            if (paramFieldIndex[splitInd] == null) {
                dependsOnParentField = true;
                //ищем подходящий родительский класс
                for (int parentDTOInd = 0; parentDTOInd < dtoNames.size(); parentDTOInd++) {
                    var parentDtoName = dtoNames.get(parentDTOInd);

                    if (paramSplitName.startsWith(parentDtoName)
                    && !(parentDtoName.length() == paramSplitName.length()
                            || paramSplitName.equals(parentDtoName + "Id")
                            || paramSplitName.equals(parentDtoName + "_Id"))) {

                        Integer parentField;
//                        if (parentDtoName.length() == paramSplitName.length()
//                                || paramSplitName.equals(parentDtoName + "Id")
//                                || paramSplitName.equals(parentDtoName + "_Id")) {
//                            parentField = 0;
//                        } else {
                            //ищем поле в родительском классе
                            String childFieldNameRest;
                            if (paramSplitName.charAt(parentDtoName.length()) == '_') {
                                childFieldNameRest = paramSplitName.substring(parentDtoName.length()
                                        + 1);
                            } else {
                                childFieldNameRest = paramSplitName.substring(parentDtoName.length());
                            }
                            parentField =
                                    dtoFieldsMap
                                            .get(parentDTOInd)
                                            .get(firstLower(childFieldNameRest));
//                        }

                        if (parentField == null) {
                            System.out.println(parentDtoName + " не подошло!");
                            continue;
                        }

                        childDTOMethodParentDTOList
                                .get(repoIndex)
                                .get(methodInd)
                                .add(parentDTOInd);
                        childDTOMethodParentFieldDTOList
                                .get(repoIndex)
                                .get(methodInd)
                                .add(parentField);
                        childDTOMethodChildMethodSplitIndParentAttributeList
                                .get(repoIndex)
                                .get(methodInd)
                                .add(splitInd);
                        break;
                    }
                }
            } else {
                fieldsLower[splitInd] = paramSplitName;

                paramFieldIndexFilled.add(paramFieldIndex[splitInd]);
            }
        }

        parameters.get(repoIndex).add(fieldsLower);


        System.out.println("997 parameterDTOFieldInds --- " + dtoNames.get(repoIndex) + " " + Arrays.toString(paramFieldIndex));
//        if (dependsOnParentField) {
//            parameterDTOFieldInds.get(repoIndex).add(paramFieldIndexFilled.toArray(new Integer[0]));
//        } else {
//            parameterDTOFieldInds.get(repoIndex).add(paramFieldIndex);
//        }
        parameterDTOFieldInds.get(repoIndex).add(paramFieldIndex);

        mapRepos.get(repoIndex).add(new ConcurrentHashMap<>());

//        System.out.println("set@ " + dtoNames.get(repoIndex) + " " + findMethodName + " " + methodInd);
        //сетим порядковый номер метода по названию
        methodOrders.get(repoIndex).put(findMethodName, methodInd);

        if (dependsOnParentField) {
            List<Integer> cdc = new ArrayList<>();
            for (int i = 0; i < paramFieldIndex.length; i++) {
                if (paramFieldIndex[i] != null) {
                    cdc.add(i);
                }
            }
            /*System.out.println(dtoNames.get(repoIndex)
                    + " "
                    + findMethodName
                    + " Free add depends "
                    + cdc);*/

            DTOParamFreeFieldIndex.get(repoIndex).add(cdc);
        } else {
            DTOParamFreeFieldIndex.get(repoIndex).add(null);
        }

        DTOMethodDependentToIndependent.get(repoIndex).add(null);

        //DTOParamFieldIndex.get(repoIndex).add(paramFieldIndex);


        if (method.getReturnType().isAssignableFrom(List.class)) {
            collectionType.get(repoIndex).add(1);
        } else if (method.getReturnType().isAssignableFrom(Set.class)) {
            collectionType.get(repoIndex).add(2);
        } else {
            collectionType.get(repoIndex).add(0);
        }
    }


    private Object processFindAll(Integer repoIndex) {

        return idMap.get(repoIndex).values().stream().toList();
    }


    private DTO processFindById(Integer repoIndex, int id) {

        DTO dto = idMap.get(repoIndex).get(id);
        if (dto != null) {
            return dto;
        } else {

            Object entity = jpaRepositories.get(repoIndex).findById(id).orElse(null);

            System.out.println(1062);

            if (entity != null) {
                DTO foundDTO = (DTO) mapRepositories.get(repoIndex).toDto(entity);
                idMap.get(repoIndex).put(id, foundDTO);

                return foundDTO;
            }
            return null;
        }
    }

    private Object deleteById(Integer repoIndex, int id) {

        DTO dto = idMap.get(repoIndex).remove(id);
        idDeleteMap.get(repoIndex).add(id);
        return dto;
    }


    private Object processFind(Integer repoIndex, String methodName, Object[] args)
            throws Exception {

//        System.out.println(dtoNames.get(repoIndex)
//                + " processFind " + methodName
//                + " " + methodOrders.get(repoIndex)
//                + " args " + Arrays.toString(args));

        int methodOrder = methodOrders.get(repoIndex).get(methodName);
        var parentRepoInds = childDTOMethodParentMethodDTOList.get(repoIndex).get(methodOrder);

        if (!parentRepoInds.isEmpty()) {
            List<Integer> idIntersections = new ArrayList<>();


            System.out.println("@@parentRepoInds** " + parentRepoInds);

            for (int i = 0; i < parentRepoInds.size(); i++) {

                var parentRepoInd = parentRepoInds.get(i);
                var parentMethodInd = childDTOMethodParentMethodMethodList.get(repoIndex)
                        .get(methodOrder).get(i);

                var parentRequestParams = childDTOMethodParentMethodRequestParamsList.get(repoIndex)
                        .get(methodOrder).get(i);

                Set<Integer> childIds = null;

                /*if (parentMethodInd == 0) {
                    int parentId = (int) args[0];
                    childIds = mapDependant
                            .get(parentRepoInd)
                            .get(repoIndex)
                            .get(parentId);

                    System.out.println("1023 parentMethodInd " + dtoNames.get(parentRepoInd) + " " + dtoNames.get(repoIndex)
                            + " parentId " + parentId + " childIds " + childIds);

                    // если ищем по parent id - значит только единичный ответ
                    if (i == 0 && childIds != null) {
                        idIntersections.addAll(childIds);
                    }
                } else {*/
                int parentType = collectionType.get(parentRepoInd).get(parentMethodInd);

                StringJoiner parentJoiner = new StringJoiner("_");
                for (var requestParam : parentRequestParams) {
                    parentJoiner.add(args[requestParam].toString());
                }
                var parentKey = parentJoiner.toString();

                var parentResult = mapRepos
                        .get(parentRepoInd)
                        .get(parentMethodInd)
                        .get(parentKey);

//                System.out.println(" parentRepoInd " + dtoNames.get(parentRepoInd)
//                        + " repoIndex " + dtoNames.get(repoIndex) + " ---* " + Arrays.toString(args)
//                        + " parentMethodInd " + parentMethodInd
//                        + " parentRequestParams---" + parentRequestParams
//                        + " parentKey@@@" + parentKey
//                + " parentResult---" + parentResult);

                if (parentResult == null) {
                    return null;
                }

                if (parentType == 0) {
                    var parentId = ((DTO) parentResult).getId();
                    ///mapDependant - не верный выбор
                    childIds = mapDependant.get(parentRepoInd).get(repoIndex).get(parentId);

                    if (i == 0) {
                        idIntersections.addAll(childIds);
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
                //}
                if (childIds != null) {
                    if (i == 0) {
                        idIntersections.addAll(childIds);
                    } else {
                        idIntersections.retainAll(childIds);
                    }
                }
            }

            int methodIndex = DTOMethodDependentToIndependent.get(repoIndex).get(methodOrder);
            List<Integer> childParamIndexList = DTOParamFreeFieldIndex
                    .get(repoIndex)
                    .get(methodOrder);

            System.out.println(dtoNames.get(repoIndex)
                    + " "
                    + methodName
                    + "****"
                    + Arrays.toString(args)
                    + " ***@@ "
                    + childParamIndexList);

            StringJoiner joiner = new StringJoiner("_");
            for (Integer i : childParamIndexList) {
                joiner.add(args[i].toString());
            }
            var childKey = joiner.toString();

            //System.out.println(childKey + " childKey@@- "+ dtoNames.get(repoIndex) + " methodIndex " + methodIndex + " " + methodName);

            Object childObject = mapRepos.get(repoIndex).get(methodIndex).get(childKey);
            if (childObject == null) {
                Method method = jpaFindMethods.get(repoIndex).get(methodOrder);

                if (method != null) {
                    AbstractEntity entity =
                            (AbstractEntity) method.invoke(jpaRepositories.get(repoIndex), args);

                    System.out.println(1201);

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
                    if (idIntersections.contains(((DTO) childObject).getId())) {
                        return childObject;
                    }
                } else {
                    for (var e : ((HashMap<Integer, DTO>) childObject).values()) {
                        if (idIntersections.contains(e.getId())) {
                            return e;
                        }
                    }
                    return null;
                }
            } else if (returnType == 1) {

                return ((HashMap<Integer, DTO>) childObject).values().stream()
                        .filter(e -> idIntersections.contains(e.getId()))
                        .toList();
            } else if (returnType == 2) {
                return ((HashMap<Integer, DTO>) childObject).values().stream()
                        .filter(e -> idIntersections.contains(e.getId()))
                        .collect(Collectors.toSet());
            }
        }

        StringJoiner joiner = new StringJoiner("_");
        for (Object arg : args) {
            joiner.add(arg.toString());
        }
        var key = joiner.toString();

        //взяли из methods порядковый номер метода
        var resultCache = mapRepos.get(repoIndex).get(methodOrder).get(key);

//        System.out.println(dtoNames.get(repoIndex) + " ***--* " + methodName + " " + Arrays.toString(args) + " " + key + " *** " + resultCache);


        if (resultCache == null) {

//            System.out.println(methodOrder + "------ " + mapRepos.get(repoIndex).get(methodOrder));


            int type = collectionType.get(repoIndex).get(methodOrder);
            Method method = jpaFindMethods.get(repoIndex).get(methodOrder);

//            System.out.println(" key " + key + " " + dtoNames.get(repoIndex) + " " + methodName + " ---* " + methodOrder);

            if (type == 0) {
                if (method != null) {
                    AbstractEntity entity =
                            (AbstractEntity) method.invoke(jpaRepositories.get(repoIndex), args);

                    System.out.println(1260);

                    if (entity == null) {
                        return null;
                    }

                    System.out.println("mapRepositories.get(" + repoIndex+") " + dtoNames.get(repoIndex)
                            + " " + mapRepositories.get(repoIndex));

                    DTO dto = (DTO) mapRepositories.get(repoIndex).toDto(entity);
                    saveToCache(dto, repoIndex, true);
                    return dto;
                } else {
                    throw new Exception("type == 0 " + methodName);
                }
                //return save((DTO) joinPoint.proceed(), repoIndex);
            } else if (type == 1) {
                if (method != null) {

                    System.out.println(1271);

                    List<AbstractEntity> entity =
                            (List<AbstractEntity>) method.invoke(
                                    jpaRepositories.get(repoIndex),
                                    args);
                    var entityMapper = mapRepositories.get(repoIndex);

                    var result = new ArrayList<DTO>(entity.size());
                    for (AbstractEntity abstractEntity : entity) {
                        result.add(saveToCache(
                                (DTO) entityMapper.toDto(abstractEntity),
                                repoIndex,
                                true));
                    }
                    return result;
                } else {
                    throw new Exception("type == 1 " + methodName);
                }
            } else if (type == 2) {
                if (method != null) {
                    Set<AbstractEntity> entity =
                            (Set<AbstractEntity>) method.invoke(
                                    jpaRepositories.get(repoIndex),
                                    args);
                    var entityMapper = mapRepositories.get(repoIndex);

                    System.out.println(1297);

                    var result = new HashSet<DTO>(entity.size());
                    for (AbstractEntity abstractEntity : entity) {
                        result.add(saveToCache(
                                (DTO) entityMapper.toDto(abstractEntity),
                                repoIndex,
                                true));
                    }
                    return result;
                } else {
                    throw new Exception("type == 2 " + methodName);
                }
            }
            return null;
        } else {
            int type = collectionType.get(repoIndex).get(methodOrder);

            if (type == 0) {
                return resultCache;
            } else if (type == 1) {
                return new ArrayList<DTO>(((HashMap) resultCache).values());
            } else if (type == 2) {
                return new HashSet<DTO>(((HashMap) resultCache).values());
            }
            return null;
        }
    }

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
        idUpdateMap.get(repoIndex).put(id, dto);

        return saveToCache(dto, repoIndex, false);
    }



    private List<ConcurrentHashMap<Integer, List<Integer>>> blocks = new ArrayList<>();
    private List<LinkedHashMap<Integer, List<Boolean>>> queues = new ArrayList<>();
    {
        blocks.add(new ConcurrentHashMap<>());
        queues.add(new LinkedHashMap<>());
    }
    {
        for (int i = 0; i < parentDTOBlockArrIndex.size(); i++) {

        }
    }
    private int[] addToQueue(int repoIndex) {
        final List<Boolean> blockedRepos = parentDTOBlockDTOIndex.get(repoIndex);
        final List<Integer> blockedArr = parentDTOBlockArrIndex.get(repoIndex);

        for (int queueIndex = 0; queueIndex < blocks.size(); queueIndex++) {
            var block = blocks.get(queueIndex);
            var queue = queues.get(queueIndex);

            int order = block.size();
            boolean cont = false;
            List<Integer> blockingOrders = new ArrayList<>();
            for (Map.Entry<Integer, List<Boolean>> entry : queue.entrySet()) {
                Integer k = entry.getKey();
                List<Boolean> blockedIndexes = entry.getValue();
                for (int i = 0; i < blockedArr.size(); i++) {
                    if (blockedIndexes.get(blockedArr.get(i))) {

                        blockingOrders.add(k);
                        cont = true;
                    }
                }
            }
            if (cont) {
                block.put(order, blockingOrders);
                queue.put(order, blockedRepos);
                return new int[] {queueIndex, order} ;
            }
        }
        return null;
    }
    private void removeFromQueue(int queueIndex, int order, int repoIndex) {
        var block = blocks.get(queueIndex);
        boolean startIterating = false;
        for (Map.Entry<Integer, List<Integer>> entry : block.entrySet()) {
            if (startIterating) {
                List<Integer> blockArr = entry.getValue();
                blockArr.remove(order);
                if (blockArr.size() == 0) {
                    //startOrder(entry.getKey());
                    //processSave(dto, repoIndex, cache);
                    break;
                }
            } else if (entry.getKey().equals(order)) {
                startIterating = true;
            }
        }
        blocks.get(queueIndex).remove(order);
        queues.get(queueIndex).remove(order);
    }
    private DTO processSave(DTO dto, Integer repoIndex, boolean cache)
            throws IllegalAccessException, NoSuchFieldException {
        int[] r = addToQueue(repoIndex);
        int queueIndex = r[0];
        int order = r[1];
        return saveToCache(dto, repoIndex, cache/*, queueIndex, order*/);
    }


    private DTO saveToCache(DTO dto, Integer repoIndex, boolean cache/*, int queueIndex, int order*/)
            throws IllegalAccessException, NoSuchFieldException {
        //если новый обьект
        if (dto.getFieldValues() == null) {

            var fieldValues = new ArrayList<>();
            Field idField;
            if (dto.getClass().getSuperclass().getSuperclass().getSimpleName().equals("DTO")) {
                idField = dto.getClass().getSuperclass().getSuperclass().getDeclaredField("id");
            } else {
                idField = dto
                        .getClass()
                        .getSuperclass()
                        .getSuperclass()
                        .getSuperclass()
                        .getDeclaredField("id");
            }

            idField.setAccessible(true);
            Integer idValue = (Integer) idField.get(dto);
            idMap.get(repoIndex).put(idValue, dto);

            fieldValues.add(dto.getId());

            int fieldIndex = 0;
            for (var fieldName : dtoFieldNames.get(repoIndex)) {

                Field field = getField(dto, repoIndex, fieldIndex, fieldName);

                var fieldValue = field.get(dto);
                fieldValues.add(fieldValue);

                var parentDT0Ind = childDTOFieldParentDTOIndex.get(repoIndex).get(fieldIndex);

//                System.out.println("1398new " + dtoNames.get(repoIndex) + " fieldName " + fieldName
//                        + " parentDT0Ind " + parentDT0Ind + " fieldValue " + fieldValue );
//                if (parentDT0Ind != null) {
//                    System.out.println(" parentDT0Ind " + dtoNames.get(parentDT0Ind) );
//                }

//                System.out.println(" fieldName---" + fieldName + " "  + fieldValue
//                        + " child "+ dtoNames.get(repoIndex)
//                        + " parentDT0Ind " + parentDT0Ind);

                if (parentDT0Ind != null && fieldValue != null) {

                    //System.out.println();

                    int parentDTOId = (Integer) fieldValue;
                    var set = mapDependant.get(parentDT0Ind).get(repoIndex).get(parentDTOId);
                    if (set == null) {
                        set = new HashSet<>();
                        mapDependant
                                .get(parentDT0Ind)
                                .get(repoIndex)
                                .put((Integer) fieldValue, set);
                    }
                    set.add(dto.getId());

                    final DTO parentDTO = idMap.get(parentDT0Ind).get(parentDTOId);

//                    System.out.println("parentDT0Ind " + dtoNames.get(parentDT0Ind) + " repoIndex " + dtoNames.get(repoIndex) +
//                            " dto " + dto + " parentDTOId-*-*- " + parentDTOId + "---"  + listDtos[parentDT0Ind][repoIndex]);
//                    System.out.println(parentDTO);

                    if (listDtos[parentDT0Ind][repoIndex] != null) {

                        addChildToParent(
                                parentDTO,
                                listDtos[parentDT0Ind][repoIndex],
                                dto);
                    } else {
                        System.out.println("Нет коллекции дочернего класса " + dtoNames.get(repoIndex)
                                + " у родительского " + dtoNames.get(parentDT0Ind));
                    }
                }
                fieldIndex++;
            }

            dto.setFieldValues(fieldValues);
            //var dtoFields = dtoFieldNames.get(repoIndex);

            var keys = new ArrayList<String>();
            dtoKeys.get(repoIndex).put(dto.getId(), keys);


            for (int i = 0; i < parameters.get(repoIndex).size(); i++) {
                var parameter = parameters.get(repoIndex).get(i);
                var paramFieldInd = parameterDTOFieldInds.get(repoIndex).get(i);
                var submap = mapRepos.get(repoIndex).get(i);
                List<String> newKeyArr = new ArrayList<>();//String[parameter.length];
                int paramIndex = 0;
                for (String param : parameter) {
                    if (param != null && !param.equals("id")) {

                        Field field = getField(dto, repoIndex, paramFieldInd[paramIndex], param);
                        var newFieldValue = field.get(dto);

//                        System.out.println(" dto " + dto
//                                + " value " + newFieldValue  + dtoNames.get(repoIndex)
//                                        + " paramFieldInd " + paramFieldInd[paramIndex]
//                                        + " param " + param
//                                + " dtoSuperclassFieldStart " + dtoSuperclassFieldStart.get(repoIndex));

                        if (newFieldValue == null) {
                            newKeyArr.add("-");
                        } else {
                            newKeyArr.add(newFieldValue.toString());
                        }
                    }
                    paramIndex++;
                }
                String newKey = String.join("_", newKeyArr);

//                System.out.println("newKey: " + newKey + " " + dtoNames.get(repoIndex) + " ***i " + i
//                        + " newKeyArr-" + newKeyArr.size() + " " + Arrays.toString(parameter));

                int type = collectionType.get(repoIndex).get(i);
                if (type == 0) {
                    submap.put(newKey, dto);
                } else {
                    var markMap = (HashMap<Integer, DTO>) submap.get(newKey);
                    if (markMap == null) {
                        markMap = new HashMap<>();
                        submap.put(newKey, markMap);
                    }
                    markMap.put(dto.getMark(), dto);
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

//                System.out.println("@1487  oldId " + oldId + " newFieldIdValue " + newFieldIdValue);

                dtoKeys.get(repoIndex).put(
                        newFieldIdValue,
                        dtoKeys.get(repoIndex).remove(oldId));

                var chDTO = mapDependant.get(repoIndex);


//                System.out.println(dtoNames.get(repoIndex) + " -@@***" + chDTO.size());

                for (int childInd = 0; childInd < chDTO.size(); childInd++) {

                    Map<Integer, Set<Integer>> parentChildren = chDTO.get(childInd);

                    //все id дочерних -
                    var dependentIds = parentChildren.get(oldId);

//                    System.out.println("oldId " + oldId + " dependentIds " + dependentIds);

                    parentChildren.put(newFieldIdValue, dependentIds);

                    if (dependentIds == null || dependentIds.size() == 0) {
                        continue;
                    }


//                    System.out.println("saveToCache repoIndex@@@ " + repoIndex + " " + parentDTOChildDTOIndex.get(repoIndex));
//                    System.out.println("dependentIds " + dependentIds + " childInd " + childInd);
//                    System.out.println("parentChildren " + parentChildren);
//                    System.out.println("saveToCache childInd@@@ " + childInd + " " + parentDTOChildDTOIndex.get(repoIndex).get(childInd));

                    //int childRepoIndex = parentDTOChildDTOIndex.get(repoIndex).get(childInd);
                    int fieldIndex = parentDTOChildDTOFieldIndex.get(repoIndex).get(childInd);
                    String fieldName = dtoFieldNames.get(childInd)[fieldIndex];

                    for (var dependentId : dependentIds) {
                        DTO childDto = idMap.get(childInd).get(dependentId);

//                        System.out.println(" @@dependentId " + dependentId + " childId " + dtoNames.get(childRepoIndex) + " " + childDto);

                        Field field = getField(childDto, childInd, fieldIndex, fieldName);

                        field.set(childDto, newFieldIdValue);
                    }

                    List<Integer> methodList = parentDTOChildMethodIndex
                            .get(repoIndex)
                            .get(childInd);
                    List<Integer> paramList = parentDTOChildParamIndex.get(repoIndex).get(childInd);
                    var paramField = parameterDTOFieldInds.get(childInd);

                    var childMap = mapRepos.get(childInd);

                    for (int methodI = 0; methodI < methodList.size(); methodI++) {

                        int methodInd = methodList.get(methodI);
                        int paramInd = paramList.get(methodI);

                        var parameter = paramField.get(methodInd);

                        boolean containsNull = false;
                        for (Integer i : parameter) {
                            if (i == null) {
                                containsNull = true;
                                break;
                            }
                        }
                        if (containsNull) {
                            continue;
                        }


//                        System.out.println(dtoNames.get(repoIndex)  + " child " + dtoNames.get(childRepoIndex) + " parameter------------" + Arrays.toString(parameter));


                        //значения по методу поиска
                        var childSubmap = childMap.get(methodInd);

                        String[] newKeyArr = new String[parameter.length];
                        String[] oldKeyArr = new String[parameter.length];

                        int childRepoMethodType = collectionType.get(childInd).get(methodInd);

                        for (var dependentId : dependentIds) {

                            DTO childDto = idMap.get(childInd).get(dependentId);

                            //метод поиска find
                            for (int j = 0; j < parameter.length; j++) {
                                //порядок в мапе полей значений dto
                                Integer fieldInd = parameter[j] + 1;
                                //индекс измененного поля
                                final Object o = childDto.getFieldValues().get(fieldInd);
                                if (o == null) {
                                    oldKeyArr[j] = "-";
                                } else {
                                    oldKeyArr[j] = o.toString();
                                }

                                if (j == paramInd) {
                                    newKeyArr[j] = Integer.valueOf(newFieldIdValue).toString();
                                } else {
                                    newKeyArr[j] = oldKeyArr[j];
                                }
                            }

//                            System.out.println(childRepoMethodType + " key " + String.join(
//                                    "_",
//                                    oldKeyArr) + "--*-*-" + childSubmap.get(String.join(
//                                    "_",
//                                    oldKeyArr)));

                            var newKey = String.join("_", newKeyArr);
                            if (childRepoMethodType == 0) {

                                childSubmap.put(newKey, dto);
                            } else {

//                                System.out.println(childSubmap);
//                                System.out.println("@@@@@@----------" + String.join("_", oldKeyArr));

                                ((HashMap<Integer, DTO>) childSubmap
                                        .get(String.join("_", oldKeyArr)))
                                        .remove(childDto.getMark());

                                var byKey = (HashMap<Integer, DTO>) childSubmap.get(newKey);
                                if (byKey == null) {
                                    byKey = new HashMap<>();
                                    childSubmap.put(newKey, byKey);
                                }
                                byKey.put(childDto.getMark(), childDto);
                            }
                        }
                    }
                }

                dto.getFieldValues().set(0, newFieldIdValue);
            }

            for (int fieldIndex = 0; fieldIndex < fieldSize; fieldIndex++) {

                var fieldName = dtoFields[fieldIndex];

                Field field = getField(dto, repoIndex, fieldIndex, fieldName);

                var newFieldValue = field.get(dto);
                var oldValue = dto.getFieldValues().get(fieldIndex + 1);

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

                    if (parentDT0 != null) {

                        Set<Integer> oldSet = mapDependant
                                .get(parentDT0)
                                .get(repoIndex)
                                .get((Integer) oldValue);
                        if (oldSet != null) {
                            mapDependant
                                    .get(parentDT0)
                                    .get(repoIndex)
                                    .get((Integer) oldValue)
                                    .remove(dto.getId());
                        }

                        Set<Integer> newSet = mapDependant
                                .get(parentDT0)
                                .get(repoIndex)
                                .get((Integer) newFieldValue);
                        if (newSet == null) {
                            newSet = new HashSet<>();
                            mapDependant
                                    .get(parentDT0)
                                    .get(repoIndex)
                                    .put((Integer) newFieldValue, newSet);
                        }
                        newSet.add(dto.getId());
                    }
                }
                //fieldIndex++;
            }

            var keys = dtoKeys.get(repoIndex).get(dto.getId());

            //репозиторий - метод поиска find
            for (int methodInd = 0; methodInd < parameterDTOFieldInds
                    .get(repoIndex)
                    .size(); methodInd++) {
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


                int type = collectionType.get(repoIndex).get(methodInd);
                if (type == 0) {
                    submap.remove(String.join("_", oldKeyArr), dto);
                    submap.put(newKey, dto);

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

                    } else {
                        n.put(dto.getMark(), dto);
                    }
                }
            }
        }

        final String[] repoFieldCollectionNames = dtoFieldCollectionNames.get(repoIndex);

        for (int i = 0; i < repoFieldCollectionNames.length; i++) {

            Field field = getField(dto, repoIndex, i, repoFieldCollectionNames[i]);

            List<DTO> fieldValues = (List<DTO>) field.get(dto);
            int childDTOIndex = dtoFieldCollectionOrder.get(repoIndex)[i];

            for (DTO fieldValue : fieldValues) {
                if (cache) {
                    saveToCache(fieldValue, childDTOIndex, true);
                } else {
                    save(fieldValue, childDTOIndex);
                }
            }
        }

        //removeFromQueue(queueIndex, order, repoIndex);

        return dto;
    }

    private void addChildToParent(DTO parentDTO, String fieldName, DTO dto)
            throws IllegalAccessException, NoSuchFieldException {

        // по идее у родительских классов не должно быть каких-то коллекций 1-M
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


    private Field getField(DTO dto, int repoIndex, int fieldIndex, String fieldName)
            throws NoSuchFieldException {
        Field field;
        if (dtoSuperclassFieldStart.get(repoIndex) <= fieldIndex) {
            field = dto.getClass().getSuperclass().getDeclaredField(fieldName);
        } else {
            field = dto.getClass().getDeclaredField(fieldName);
        }
        field.setAccessible(true);
        return field;
    }

    private void processParentDTO() {

        for (int parentDTOInd = 0; parentDTOInd < dtoNames.size(); parentDTOInd++) {

            Set<String> childDTOAndField = new HashSet<>();
            for (int childDTOInd = 0; childDTOInd < dtoFieldNames.size(); childDTOInd++) {

                int fieldSize = dtoFieldNames.get(childDTOInd).length;

                for (int childFieldInd = 0; childFieldInd < fieldSize; childFieldInd++) {

                    var childFieldName = cleanId(dtoFieldNames.get(childDTOInd)[childFieldInd]);

                    if (childFieldName.equals(dtoNames.get(parentDTOInd))) {

                        System.out.println("A@@* " + dtoNames.get(parentDTOInd) + " " + dtoNames.get(childDTOInd) + " " + childFieldInd);

                        childDTOAndField.add(childDTOInd + "+" + childFieldInd);

                        processChildDTO(parentDTOInd, childDTOInd, childFieldInd);
                    }
                }
            }

            for (int childDTOInd = 0; childDTOInd < dtoFieldNames.size(); childDTOInd++) {
                for (int childFieldInd = 0; childFieldInd < dtoFieldNames.get(childDTOInd).length; childFieldInd++) {

                    var childFieldName = cleanId(dtoFieldNames.get(childDTOInd)[childFieldInd]);

                    String[] parts = StringUtils.splitByCharacterTypeCamelCase(childFieldName);
                    String part = firstLower(parts[parts.length - 1]);
//                    System.out.println("@@@@----***" + dtoClasses.get(childDTOInd) + " @childFieldName " + childFieldName);

//                    for (Field declaredField : dtoClasses.get(childDTOInd).getDeclaredFields()) {
//                        System.out.println(declaredField.getName());
//                    }
//
//                    System.out.println(dtoClasses.get(childDTOInd).getDeclaredField(childFieldName));

//                    System.out.println("@@@@----***" + dtoClasses.get(childDTOInd).getField(childFieldName).getType().getSimpleName());
                    if (part.equals(dtoNames.get(parentDTOInd))
                            && !childDTOAndField.contains(childDTOInd + "+" + childFieldInd)
                    && isInteger(childDTOInd, childFieldName)) {

                        System.out.println("B@* " + dtoNames.get(parentDTOInd) + " " + dtoNames.get(childDTOInd) + " " + childFieldInd
                                + " --- " + childFieldName);

                        childDTOAndField.add(childDTOInd + "+" + childFieldInd);

                        processChildDTO(parentDTOInd, childDTOInd, childFieldInd);
                    }
                }
            }
        }
    }

    private boolean isInteger(int childDTOInd, String childFieldName) {
        Field declaredField = null;
        try {
            declaredField = dtoClasses.get(childDTOInd)
                    .getDeclaredField(childFieldName);
        } catch (NoSuchFieldException e) {
            try {
                declaredField = dtoClasses.get(childDTOInd)
                        .getSuperclass()
                        .getDeclaredField(childFieldName);
            } catch (NoSuchFieldException e1) {

                System.out.println("Нет поля *** " + dtoClasses.get(childDTOInd) + " " + childFieldName);
            }
        }

        return declaredField != null && "Integer".equals(declaredField.getType().getSimpleName());
    }

    private void processChildDTO(int parentDTOInd, int childDTOInd, int childFieldInd) {

        parentDTOBlockDTOIndex.get(parentDTOInd).set(childDTOInd, true);
        parentDTOBlockArrIndex.get(parentDTOInd).add(childDTOInd);


//        parentDTOChildDTOIndex.get(parentDTOInd).add(childDTOInd);
        setList(parentDTOChildDTOFieldIndex.get(parentDTOInd), childDTOInd, childFieldInd);
//        parentDTOChildDTOFieldIndex.get(parentDTOInd).add(childFieldInd);

        childDTOFieldParentDTOIndex
                .get(childDTOInd)
                .set(childFieldInd, parentDTOInd);

        //просто список методов, где используется поле
        //                                 дочерний dto     зависимое поле
        int size = DTOFieldMethodIndex
                .get(childDTOInd)
                .get(childFieldInd)
                .size();
        for (int y = 0; y < size; y++) {
            int childMethod = DTOFieldMethodIndex
                    .get(childDTOInd)
                    .get(childFieldInd)
                    .get(y);
            int childParam = DTOFieldParamIndex
                    .get(childDTOInd)
                    .get(childFieldInd)
                    .get(y);

            //а это список методов для дочернего dto, где используется ссылка на поле
            parentDTOChildMethodIndex.get(parentDTOInd).get(childDTOInd)
                    .add(childMethod);
            //а это список параметров - парных для мтеодов для дочернего dto,
            // где используется ссылка на поле
            parentDTOChildParamIndex.get(parentDTOInd).get(childDTOInd)
                    .add(childParam);
        }
    }

    private String cleanId(String childFieldName) {
        if (childFieldName.endsWith("_Id")) {
            return childFieldName.substring(0, childFieldName.length() - 3);
        } else if (childFieldName.endsWith("Id")) {
            return childFieldName.substring(0, childFieldName.length() - 2);
        }
        return childFieldName;
    }

    private <T> List<T> setList(List<T> list, int pos, T t) {

        int initialSize = list.size();
        for (int i = 0; i < pos - initialSize + 1; i++) {
            list.add(null);
        }
        list.set(pos, t);
        return list;
    }

    public static void main(String[] args) {

        String[] parts = StringUtils.splitByCharacterTypeCamelCase("dsdaWsadasd");

        System.out.println(Arrays.toString(parts));

        System.out.println();

        System.out.println(123);
    }
}
