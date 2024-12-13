package controller;

import view.IntroView;

public class Main {
	public static void main(String[] args) {
		IntroView introView = new IntroView();
		Controller.setIntroView(introView); 
		introView.setVisible(true);
	}
}

