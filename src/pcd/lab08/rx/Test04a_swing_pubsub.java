package pcd.lab08.rx;

import javax.swing.*;

import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.awt.event.*;
import java.util.concurrent.TimeUnit;

public class Test04a_swing_pubsub {	

	static class MyFrame extends JFrame {	

		private PublishSubject<Integer> stream;
		
		public MyFrame(PublishSubject<Integer> stream){
			super("Swing + RxJava");
			this.stream = stream;
			setSize(150,60);
			setVisible(true);
			JButton button = new JButton("Press me");
			button.addActionListener((ActionEvent ev) -> {
				// produce elementi nello stream, come se fosse la put
				stream.onNext(1);
			});
			getContentPane().add(button);
			addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent ev){
					System.exit(-1);
				}
			});
		}
	}

	static public void main(String[] args){

		// è un observable, in cui
		PublishSubject<Integer> clickStream = PublishSubject.create();
		
		SwingUtilities.invokeLater(()->{
			new MyFrame(clickStream);
		});

		clickStream
			.observeOn(Schedulers.computation())
			.subscribe((v) -> {
				System.out.println(Thread.currentThread().getName() + "click: "+System.currentTimeMillis());
			});

		clickStream
				// buffer permette di raggruppare n item di una finestra temporale per far in modo che n item siano visti
				// come un unico item
			.buffer(clickStream.throttleWithTimeout(250, TimeUnit.MILLISECONDS))
			.map(xs -> xs.size())
			.filter((v) -> v >= 2)
			.subscribe((v) -> {
				System.out.println(Thread.currentThread().getName() + ": Multi-click: "+v);
			});
		
	}

}
