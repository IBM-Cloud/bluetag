package com.bluetag.api.game.logic;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Listener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("GAME OVER");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		GameLogicThread gameLT = new GameLogicThread();
		gameLT.start();
	}
	
}
