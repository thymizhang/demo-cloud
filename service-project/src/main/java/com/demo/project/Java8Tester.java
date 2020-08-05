package com.demo.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Java8特性测试
 *
 * @Author thymi
 * @Date 2020/5/14
 */
public class Java8Tester {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Project {
        String name;
        int num;
        String note;
    }

    public static void main(String args[]) {

        List<Project> projects = new ArrayList<>();
        projects.add(new Project("天河大桥", 10, ""));
        projects.add(new Project("海珠公园", 15, "有地铁"));
        projects.add(new Project("南沙广场", 28, ""));
        projects.add(new Project("荔湾大厦", 18, ""));
        projects.add(new Project("黄埔医院", 11, "停车场"));
        projects.add(new Project("番禺城轨", 19, ""));
        projects.add(new Project("增城地铁", 25, "21号线"));
        projects.add(new Project("增城地铁", 25, "21号线"));
        System.out.println("======原始数据 " + projects.stream().count());
        projects.stream().forEach(s -> System.out.println(s));
        System.out.println();

        Java8Tester tester = new Java8Tester();

        //tester.streamMerge();
        //tester.streamFilter(projects);
        //tester.streamSorted(projects);
        //tester.streamDistinct(projects);
        //tester.streamPeek(projects);
        //tester.streamFind(projects);
        //tester.streamReduce();
        //tester.streamSkipLimit(projects);
        //tester.streamFlatmap(projects);

        System.out.println(UUID.randomUUID());
    }

    /**
     * 扁平化映射, 将多维嵌套列表转换为单维列表
     * 应用: 数据清洗, 提取文本词汇, 并去重
     */
    public void streamFlatmap(List<Project> projects) {
        System.out.println("======stream测试: 字符串通过空格Split获取单次，并对所有单词去重");
        System.out.println("======原数据");
        Stream<String> testStream = Stream.of("hello welcome", "world hello", "hello world", "hello world welcome");
        testStream.flatMap(str -> Arrays.stream(str.split(" "))).forEach(s -> System.out.println(s));
        System.out.println("======去重");
        Stream<String> testStream2 = Stream.of("hello welcome", "world hello", "hello world", "hello world welcome");
        testStream2.flatMap(str -> Arrays.stream(str.split(" "))).distinct().forEach(s -> System.out.println(s));
    }

    /**
     * 用skip和limit取代sublist
     * 应用: 获取集合中的某一段对象
     */
    public void streamSkipLimit(List<Project> projects) {
        System.out.println("======stream测试: 去掉前面n个对象");
        projects.stream().skip(5).forEach(s -> System.out.println(s));
        System.out.println("======stream测试: 获取前面n个对象");
        projects.stream().limit(3).forEach(System.out::println);
        System.out.println();

    }

    /**
     * 聚合操作，所有的元素归约成一个，比如对所有元素求和，乘啊等
     */
    public void streamReduce() {
        System.out.println("======stream测试: 累加器");
        Integer reduce = Stream.of(1, 2, 3, 4).reduce(0, (sum, item) -> {
            System.out.println("sum: " + sum + " item: " + item);
            return sum + item;
        });
        System.out.println(reduce);
        System.out.println();
    }

    /**
     * 获取第一个对象
     * 应用: 只需要返回第一个数据时, 简化代码
     */
    public void streamFind(List<Project> projects) {
        System.out.println("======stream测试: 获取第一个对象,如果没有返回null");
        Project project = projects.stream().findFirst().orElse(null);
        System.out.println(project.toString());
        System.out.println("======stream测试: 获取任意对象");
        Optional<Project> any = projects.stream().findAny();
        any.stream().forEach(s -> System.out.println(s));
        System.out.println();
    }

    /**
     * 去除重复的对象
     * 应用: 为数据瘦身, 很有用
     */
    public void streamDistinct(List<Project> projects) {
        System.out.println("======stream测试: 对象去重");
        projects.stream().distinct().forEach(s -> System.out.println(s));
        System.out.println();
    }

    /**
     * 为字段赋值
     *
     * @param projects
     */
    public void streamPeek(List<Project> projects) {
        System.out.println("======stream测试: 为name和num字段赋值");
        projects.stream().peek(m -> {
            m.setNum(m.getNum() * 2);
            m.setName("广州: " + m.getName());
        }).forEach(s -> System.out.println(s));
        System.out.println();
    }

    /**
     * 需要根据两个集合中id值相同，就把第二个集合中的grade值赋给第一个集合，如果不匹配，默认grade值为0
     */
    public void streamMerge() {
        System.out.println("======stream测试: 两个list条件匹配,合并为新对象");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 1; i < 9; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i);
            map.put("name", "张三丰" + i);
            list.add(map);
        }
        Stream<Map<String, Object>> s1 = list.stream();
        list.stream().forEach(map -> System.out.println(map));

        List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
        for (int i = 1; i < 5; i++) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("id", i);
            map2.put("grade", i + 60);
            list2.add(map2);
        }
        list2.stream().forEach(s -> System.out.println(s));

        List<Map<String, Object>> resultList2 = list.stream().map(m -> {
            m.put("grade", 0);
            list2.stream().filter(m2 -> Objects.equals(m.get("id"), m2.get("id"))).forEach(s -> m.put("grade", s.get("grade")));
            return m;
        }).collect(Collectors.toList());

        resultList2.stream().forEach(s -> System.out.println(s));
        System.out.println();
    }

    /**
     * 条件过滤
     *
     * @param projects
     */
    private void streamFilter(List<Project> projects) {
        System.out.println("======stream测试: 去除符合条件的对象");
        projects.stream().filter(project -> project.getNum() > 10).filter(project -> "".equals(project.getNote())).forEach(s -> System.out.println(s));
        System.out.println();
    }

    /**
     * 升降序排列
     *
     * @param projects
     */
    private void streamSorted(List<Project> projects) {
        System.out.println("======stream测试: 按num字段降序排列");
        projects.stream().sorted(Comparator.comparing(Project::getNum).reversed()).forEach(s -> System.out.println(s));
        System.out.println("======stream测试: 按num字段升序排列");
        projects.stream().sorted(Comparator.comparing(Project::getNum)).forEach(s -> System.out.println(s));
        System.out.println("======stream测试: 按note字段空值靠前排列");
        projects.stream().sorted(Comparator.nullsFirst(Comparator.comparing(Project::getNote))).forEach(s -> System.out.println(s));
        System.out.println("======stream测试: 取出name字段排列");
        projects.stream().distinct().map(m -> m.getName()).sorted().forEach(s -> System.out.println(s));
        System.out.println();
    }

    private void testStream5(List<Project> projects) {
        System.out.println("======stream测试: 保留符合条件的对象");
    }

    private List<Map<Object, Object>> compareListHitData(List<Map<Object, Object>> oneList, List<Map<Object, Object>> twoList) {
        System.out.println("======stream测试两个list条件匹配,筛选结果");
        List<Map<Object, Object>> resultList = oneList.stream().map(map -> twoList.stream()
                .filter(m -> Objects.equals(m.get("id"), map.get("id")))
                .findFirst().map(m -> {
                    map.putAll(m);
                    return map;
                }).orElse(null))
                .filter(Objects::nonNull).collect(Collectors.toList());
        return resultList;
    }
}
