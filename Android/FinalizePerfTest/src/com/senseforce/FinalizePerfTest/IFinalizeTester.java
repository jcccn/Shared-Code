package com.senseforce.FinalizePerfTest;

public interface IFinalizeTester {
	
	public void runTest();
	/**
	 * 循环测试的某次测试
	 * @param loopIndex 循环的第多少次
	 */
	public void runTest(int loopIndex);

}
