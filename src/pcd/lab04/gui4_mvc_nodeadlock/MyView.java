package pcd.lab04.gui4_mvc_nodeadlock;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// in realtà extends JFrame non va molto bene perchè la View non deve dipendere dalla tecnologia
class MyView extends JFrame implements ActionListener, ModelObserver {

	private MyController controller;
	private JTextField state;
	
	public MyView(MyController controller) {
		super("My View");
		
		this.controller = controller;
		
		setSize(400, 60);
		setResizable(false);
		
		JButton button1 = new JButton("Event #1");
		button1.addActionListener(this);

		JButton button2 = new JButton("Event #2");
		button2.addActionListener(this);
		
		state = new JTextField(10);
		
		JPanel panel = new JPanel();
		panel.add(button1);		
		panel.add(button2);	
		panel.add(state);
		
		setLayout(new BorderLayout());
	    add(panel,BorderLayout.NORTH);
	    		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(-1);
			}
		});
	}
	
	public void actionPerformed(ActionEvent ev) {
		try {
			controller.processEvent(ev.getActionCommand());
		} catch (Exception ex) {
		}
	}

	@Override
	public void modelUpdated(MyModel model) {
		try {
			System.out.println("[View] model updated => updating the view");
			// domanda esame: perchè devo mettere questo SwingUtilities.invokeLater
			// ad accedere al componente, per leggere e scrivere, lo può fare solo event dispatch thread
			// se chiamamo direttamente setText se non siamo edt, si possono creare corse critiche perchè mentre accediamo, edt potrebbe andare a modificare
			// e quindi con invokeLater dobiamo delegare il compito all'edt
			SwingUtilities.invokeLater(() -> {
				state.setText("state: " + model.getState());
			});
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
