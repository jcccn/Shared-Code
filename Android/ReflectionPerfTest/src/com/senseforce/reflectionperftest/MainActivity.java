package com.senseforce.reflectionperftest;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
    
	private TextView displayTextView = null;
	private Button button0, button1, button2, button3, button4, button5, button6;
	
	private ClassA objectA = new ClassA();
	private Field mField;
	
	public MainActivity() {
		super();
		try {
			mField = objectA.getClass().getField("field_n");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        displayTextView = (TextView)findViewById(R.id.display_textview);
        button0 = (Button)findViewById(R.id.button_0);
        button1 = (Button)findViewById(R.id.button_1);
        button2 = (Button)findViewById(R.id.button_2);
        button3 = (Button)findViewById(R.id.button_3);
        button4 = (Button)findViewById(R.id.button_4);
        button5 = (Button)findViewById(R.id.button_5);
        button6 = (Button)findViewById(R.id.button_6);
        
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
    }
    
    @Override
	public void onClick(View v) {
		if (v == button0) {
			try {
				long temp;
				
				startTime();
				temp = getLongProperty(objectA, "field_n");
//				temp = getLongProperty_cached(objectA);
				long timeReflect = stopTime();
				
				startTime();
				temp = objectA.field_n;
				long timeNormal = stopTime();
				
				startTime();
				temp = stopTime();	//用以计算误差
				
				printResult(R.string.button_title_0, timeReflect, timeNormal, temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		else if (v == button1) {
			try {
				long temp;
				
				startTime();
				temp = getStaticLongProperty("com.senseforce.reflectionperftest.ClassA", "field_m");
				long timeReflect = stopTime();
				
				startTime();
				temp = ClassA.field_m;
				long timeNormal = stopTime();
				
				startTime();
				temp = stopTime();	//用以计算误差
				
				printResult(R.string.button_title_1, timeReflect, timeNormal, temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (v == button2) {
			try {
				Object obj0 = new Object();
				Object obj1 = new Object();
				Object[] args = {obj0, obj1};
				
				startTime();
				invokeMethod(objectA, "method_x", args);
				long timeReflect = stopTime();
				
				startTime();
				objectA.method_x(obj0, obj1);
				long timeNormal = stopTime();
				
				startTime();
				long temp = stopTime();	//用以计算误差
				
				printResult(R.string.button_title_2, timeReflect, timeNormal, temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if (v == button3) {
			try {
				Object obj0 = new Object();
				Object obj1 = new Object();
				Object[] args = {obj0, obj1};
				
				startTime();
				invokeStaticMethod("com.senseforce.reflectionperftest.ClassA", "method_y", args);
				long timeReflect = stopTime();
				
				startTime();
				ClassA.method_y(obj0, obj1);
				long timeNormal = stopTime();
				
				startTime();
				long temp = stopTime();	//用以计算误差
				
				printResult(R.string.button_title_3, timeReflect, timeNormal, temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if (v == button4) {
			try {
				Object obj0 = new Object();
				Object obj1 = new Object();
				Object[] args = {obj0, obj1};
				
				startTime();
				newInstance("com.senseforce.reflectionperftest.ClassA", args);
				long timeReflect = stopTime();
				
				startTime();
				new ClassA(obj0, obj1);
				long timeNormal = stopTime();
				
				startTime();
				long temp = stopTime();	//用以计算误差
				
				printResult(R.string.button_title_4, timeReflect, timeNormal, temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if (v == button5) {
			Class<?> aClass = ClassA.class;
			startTime();
			aClass.isInstance(objectA);
			long timeReflect = stopTime();
			
			startTime();
			long temp = stopTime();	//用以计算误差
			
			printResult(R.string.button_title_5, timeReflect, 0, temp);
		}
		
		else if (v == button6) {
			int[] anArray = {1,2,3};
			startTime();
			Array.get(anArray, 0);
			long timeReflect = stopTime();
			
			startTime();
			long temp = stopTime();	//用以计算误差
			
			printResult(R.string.button_title_6, timeReflect, 0, temp);
		}
		
	}
    
    /**
     * 得到某个对象的属性
     * @param anObject 对象
     * @param fieldName 属性名
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private long getLongProperty(Object anObject, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    	return anObject.getClass().getField(fieldName).getLong(anObject);
    }
    
    /**
     * 得到某个对象的属性(对Field做缓存，提高了性能)
     * @param anObject 对象
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private long getLongProperty_cached(Object anObject) throws IllegalArgumentException, IllegalAccessException {
    	return mField.getLong(anObject);
    }
    
    /**
     * 得到某个类的静态属性
     * @param className	类名
     * @param fieldName 属性名
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private long getStaticLongProperty(String className, String fieldName) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
    	Class<?> aClass = Class.forName(className);
    	return aClass.getField(fieldName).getLong(aClass);
    }
    
    /**
     * 执行某对象的方法
     * @param anObject 对象
     * @param methodName 方法名
     * @param args 参数数组
     * @return
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private Object invokeMethod(Object anObject, String methodName, Object[] args) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	@SuppressWarnings("rawtypes")
		Class[] argClasses = new Class[args.length];
    	for (int i = 0, j = args.length; i < j; i++) {   
    		argClasses[i] = args[i].getClass();   
        }
    	return anObject.getClass().getMethod(methodName, argClasses).invoke(anObject, args);
    }
    
    /**
     * 执行某个类的静态方法
     * @param className 类名
     * @param methodName 方法名
     * @param args 参数数组
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private Object invokeStaticMethod(String className, String methodName, Object[] args) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	@SuppressWarnings("rawtypes")
		Class[] argClasses = new Class[args.length];
    	for (int i = 0, j = args.length; i < j; i++) {   
    		argClasses[i] = args[i].getClass();   
        }
    	Class<?> aClass = Class.forName(className); 
    	return aClass.getMethod(methodName, argClasses).invoke(null, args);
    }
    
    /**
     * 新建实例
     * @param className 类名
     * @param args 构造参数数组
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private Object newInstance(String className, Object[] args) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	@SuppressWarnings("rawtypes")
    	Class[] argClasses = new Class[args.length];
    	for (int i = 0, j = args.length; i < j; i++) {   
    		argClasses[i] = args[i].getClass();   
        }
    	Class<?> aClass = Class.forName(className);
    	return aClass.getConstructor(argClasses).newInstance(args);
    }
    
    
    private long mStartTime = 0;
    /**
     * 计时开始
     */
    private void startTime() {
    	mStartTime = System.nanoTime();
    }
    
    /**
     * 计时结束
     * @return 距离上次计时开始的时间间隔
     */
    private long stopTime() {
    	long stopTime = System.nanoTime();
    	long interval = stopTime - mStartTime;
    	mStartTime = stopTime;
    	return interval;
    }
    
    /**
     * 打印测试结果
     * @param tag 测试的项目名
     * @param nanoSecondReflect 反射调用花费的纳秒数
     * @param nanoSecondNormal 普通调用话费的纳秒数
     * @param nanoError 执行测试方法产生的误差纳秒数
     */
    private void printResult(String tag, long nanoSecondReflect, long nanoSecondNormal, long nanoError) {
    	displayTextView.setText(tag + " (nano second):\n" + "reflect:" + nanoSecondReflect + "\nnormal:" + nanoSecondNormal  + "\nerror:" + nanoError);
    }
    
    private void printResult(int tagResId, long nanoSecondReflect, long nanoSecondNormal, long nanoError) {
    	printResult(getString(tagResId), nanoSecondReflect, nanoSecondNormal, nanoError);
    }
    
    @Override
    public void onDestroy() {
    	displayTextView = null;
    	button0.setOnClickListener(null);
        button1.setOnClickListener(null);
        button2.setOnClickListener(null);
        button3.setOnClickListener(null);
        button4.setOnClickListener(null);
        button5.setOnClickListener(null);
        button6.setOnClickListener(null);
    	super.onDestroy();
    }

}