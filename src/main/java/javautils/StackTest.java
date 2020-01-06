package main.java.javautils;

import java.util.Iterator;
import java.util.List;

/**
 * @desc Stack的测试程序。测试常用API的用法
 *
 * @author skywang
 */
public class StackTest {

    public static void main(String[] args) {
        /*Stack stack = new Stack();
        // 将1,2,3,4,5添加到栈中
        for(int i=1; i<6; i++) {
            stack.push(String.valueOf(i));
        }

        // 遍历并打印出该栈
        iteratorThroughRandomAccess(stack) ;

        // 查找“2”在栈中的位置，并输出
        int pos = stack.search("2");
        System.out.println("the postion of 2 is:"+pos);

        // pup栈顶元素之后，遍历栈
        stack.pop(); // 去除栈
        System.out.println("pop");
        iteratorThroughRandomAccess(stack) ;

        // peek栈顶元素之后，遍历栈
        String val = (String)stack.peek();
        System.out.println("peek:"+val);
        iteratorThroughRandomAccess(stack) ;

        // 通过Iterator去遍历Stack
        iteratorThroughIterator(stack) ;*/
        String a = "abcdef";
        changStr(a);
        // 分析一下，java传参是引用传递
        // 针对值类型，直接传值
        // 引用类型，是将对象的引用拷贝传递到方法里
        // 如果方法里重新指向别的对象，并不能引起原对象的变化
        // String 是引用类型，但是其中的char[]是final
        // 传递后，相当于在堆中new了一个新字符串对象"123456"
        // 故我认为输出的还是abcdef
//        System.out.println(a);
        int i = 213;
        changBaseType(i);
        System.out.println(i);

    }

    /**
     * 通过快速访问遍历Stack
     */
    public static void iteratorThroughRandomAccess(List list) {
        String val = null;
        for (int i=0; i<list.size(); i++) {
            val = (String)list.get(i);
            System.out.print(val+" ");
        }
        System.out.println();
    }

    /**
     * 通过迭代器遍历Stack
     */
    public static void iteratorThroughIterator(List list) {

        String val = null;
        for(Iterator iter = list.iterator(); iter.hasNext(); ) {
            val = (String)iter.next();
            System.out.print(val+" ");
        }
        System.out.println();
    }

    public static void changStr(String str){
        str = "12345";
    }
    public static void changBaseType(int i){
        i = 12345;
    }


}