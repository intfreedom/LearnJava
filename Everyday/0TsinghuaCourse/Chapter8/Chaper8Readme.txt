1.Java线程；

1.1.1
进程：有独立的空间；每个word，每个IE同时运行，就是多进程；
线程：
1.1.2
模拟虚拟的CPU，封装在java.lang.Thread来实现的；
1.1.3
定义一个线程类，它继承thread，并重写run方法；
创建线程两种方式
1.1.3.1
通过Thread类：
thread.start()将自动进入run()方法；
main方法调用的run,并不影响其运行，main继续运行至结束；