包结构:

	com.bfgame.app.activity;                     activity
	com.bfgame.app.activity.adapter;              activity -adapter
	com.bfgame.app.activity.fragment;          	  fragment
	com.bfgame.app.activity.fragment.adapter;     fragment-adapter
	com.bfgame.app.base;						   基类
	com.bfgame.app.bitmap.util;					  bitmap框架
	com.bfgame.app.common;					           常量
	com.bfgame.app.db;						          数据库工具类
	com.bfgame.app.download;					   下载工具类
	com.bfgame.app.net;						          网络请求类 
	com.bfgame.app.net.exception;				   异常处理类
	com.bfgame.app.net.utils;					   网络工具类				  
	com.bfgame.app.procotol;					   请求解析类
	com.bfgame.app.service;					          服务
	com.bfgame.app.util;						  工具类
	com.bfgame.app.util.preference;       		  本地缓存工具类
	com.bfgame.app.vo;						      bean类
	com.bfgame.app.widget.dialog;				  对话框view类
	com.bfgame.app.widget.view;   				  自定义view类
	com.bfgame.app.widget.view.adapter;			  view-adapter
	org.json.simple;							  json工具类
	org.json.simple.parser;						  json解析工具类


	主要功能:  首页  分类   排行  单机  网游  礼包中心  搜索   下载管理
	
	App启动Activity：com.bfgame.app.activity.MainActivity
	
	框架说明：
	   
	       整体项目采用一个activity,整体视图界面使用viewpager+fragment
	       建立baseActivity baseFragment baseAdapter框架基类构建模板方法
	       界面中间为一个ListView，来显示当前服务器获取的最新列表
	       通过下拉刷新的方式获取服务器最新信息
	       界面切换用bundle封装数据进行传递.

	技术要点：
	
	     1.  listview 视图缓存优化,分批加载,listview监听OnScrollListener实现异步加载,adapter contentview 重用,减少对象创建,使用view 的setTag与getTag方法,listview 异步加载优化,保证listview快速显示并滚动流畅,利用线程池解决开销与资源不足的问题，本地维持任务队列,保证资源合理利用,网络图片缓存优化 ImageLoader 图片加载器完成,用afinal框架FinalBitmap模块 配置线程池数量，缓存大小,缓存路径,加载显示动画等,内存优化,网络图片比例缩小,对图片进行软引用 ,优化Dalvik虚拟机堆内存分配
	     2.  httpclient与服务器通信,判断网络类型,若为手机上网代理信息封装,listview优化分批加载，实现自定义控件下拉刷新，利用ormlite的ORM框架实现本地缓存,数据持久化存储
	     3.  获取网络数据属于耗时操作,避免ANR异常，多个请求启动多个线程,系统将不堪重复，AsyncTask异步加载访问服务器
	     4.  异常日志处理,数据统计，logUtils工具类处理
	 

