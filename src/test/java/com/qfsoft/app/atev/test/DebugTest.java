package com.qfsoft.app.atev.test;

import org.springframework.boot.SpringApplication;

import com.qfsoft.app.atev.MainSpring;

import junit.framework.TestCase;
import qfsoft.library.common.method.DealThread;

public class DebugTest extends TestCase {
	
	protected void setUp() throws Exception {
	}
	
	protected void tearDown() throws Exception {
	}
	
	public void testApp() throws Exception {
		String[] args=new String[] {};
		SpringApplication.run(MainSpring.class, args);
		DealThread.pause(1000*60*60);
	}
	
}
