package gui;

import java.awt.Container;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import core.rulesets.NegativeHeartsRuleset;
import core.rulesets.NegativeKingRuleset;
import core.rulesets.NegativeLastTwoRuleset;
import core.rulesets.NegativeMenRuleset;
import core.rulesets.NegativeTricksRuleset;
import core.rulesets.NegativeWomenRuleset;
import core.rulesets.Ruleset;

@SuppressWarnings("serial")
public class GameSelectScreen extends JFrame{
	
	// Constants
		private final int WIDTH = 1024;
		private final int HEIGHT = 768;
		private final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green
		private JRadioButton b1;
		private JRadioButton b2;
		private JRadioButton b3;
		private JRadioButton b4;
		private JRadioButton b5;
		private JRadioButton b6;

		public GameSelectScreen() {
			super();
			initializeJFrame();
			initializeContentPane();
			Container contentPane = this.getContentPane();
			
			initializeSelectCombobox();
			
			contentPane.validate();
			contentPane.repaint();
		}

		private void initializeSelectCombobox() {
			
			b1 = new JRadioButton("Neg Vazas");
			b1.setBounds(75,50,150,20);    
			b1.setSelected(true);
			b2 = new JRadioButton("Neg Copas");
			b2.setBounds(75,70,150,20);
			b3 = new JRadioButton("Neg Homens");
			b3.setBounds(75,90,150,20);
			b4 = new JRadioButton("Neg Mulheres");
			b4.setBounds(75,110,150,20);
			b5 = new JRadioButton("Neg 2U");
			b5.setBounds(75,130,150,20);
			b6 = new JRadioButton("Neg King");
			b6.setBounds(75,150,150,20);
			
			ButtonGroup bg=new ButtonGroup();    
			bg.add(b1);
			bg.add(b2);
			bg.add(b3);
			bg.add(b4);
			bg.add(b5);
			bg.add(b6);
			
			this.getContentPane().add(b1);
			this.getContentPane().add(b2);
			this.getContentPane().add(b3);
			this.getContentPane().add(b4);
			this.getContentPane().add(b5);
			this.getContentPane().add(b6);
			
			JButton playGameButton = new JButton();
			this.getContentPane().add(playGameButton);
			
			playGameButton.addActionListener(new GameSelectActionListener());
			playGameButton.setBounds(230, 50, 120, 120);
			playGameButton.setText("Jogar");
		}

		private void initializeJFrame() {
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(WIDTH, HEIGHT);
		}

		private void initializeContentPane() {
			getContentPane().setLayout(null);
			getContentPane().setBackground(TABLE_COLOR);
		}


		class GameSelectActionListener implements java.awt.event.ActionListener {
			public void actionPerformed(java.awt.event.ActionEvent event) {
				Ruleset ruleset;
				if(b1.isSelected()) {
					ruleset = new NegativeTricksRuleset();
				}else if(b2.isSelected()){
					ruleset = new NegativeHeartsRuleset();
				}else if(b3.isSelected()){
					ruleset = new NegativeMenRuleset();
				}else if(b4.isSelected()){
					ruleset = new NegativeWomenRuleset();
				}else if(b5.isSelected()){
					ruleset = new NegativeLastTwoRuleset();
				}else{
					ruleset = new NegativeKingRuleset();
				}
				new GameMode(ruleset);
			}
		}

}
