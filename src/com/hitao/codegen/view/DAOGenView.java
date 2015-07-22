//package com.hitao.codegen.view;
//
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.io.File;
//
//import javax.swing.BorderFactory;
//import javax.swing.ButtonGroup;
//import javax.swing.JButton;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JRadioButton;
//import javax.swing.JScrollPane;
//import javax.swing.JTextField;
//import javax.swing.JTree;
//import javax.swing.SwingUtilities;
//import javax.swing.UIManager;
//import javax.swing.border.EtchedBorder;
//import javax.swing.tree.DefaultMutableTreeNode;
//import javax.swing.tree.DefaultTreeModel;
//import javax.swing.tree.TreePath;
//import javax.swing.tree.TreeSelectionModel;
//
//import org.dyno.visual.swing.layouts.Constraints;
//import org.dyno.visual.swing.layouts.GroupLayout;
//import org.dyno.visual.swing.layouts.Leading;
//
//
////VS4E -- DO NOT REMOVE THIS LINE!
//public class DAOGenView extends JFrame {
//
//	private static final long serialVersionUID = 1L;
//	private JTree jTree0;
//	private JScrollPane jScrollPane0;
//	private DefaultMutableTreeNode configNode = new DefaultMutableTreeNode(
//			"基础配置");
//	private DefaultMutableTreeNode runNode = new DefaultMutableTreeNode("运行");
//	private JFileChooser jFileChooser0;
//	private JFileChooser jFileChooser1;
//	private JFileChooser jFileChooser2;
//	private JLabel jLabel0;
//	private JLabel jLabel1;
//	private JLabel jLabel2;
//	private JTextField jTextField0;
//	private JButton jButton0;
//	private JTextField jTextField1;
//	private JButton jButton1;
//	private JTextField jTextField2;
//	private JButton jButton2;
//	public DAOGenView() {
//		initComponents();
//	}
//
//	private void initComponents() {
//		GroupLayout layout = new GroupLayout();
//		setLayout(layout);
//		add(getJScrollPane0(), new Constraints(new Leading(3, 97, 10, 10), new Leading(0, 316, 10, 10)));
//		add(getJPanel0(), new Constraints(new Leading(103, 716, 10, 10), new Leading(3, 304, 10, 10)));
//		showRunDialog();
//		addMouseListener(new MouseAdapter() {
//	
//			public void mouseClicked(MouseEvent event) {
//				mouseMouseClicked(event);
//			}
//		});
//		setSize(814, 316);
//	}
//
//	private JButton getJButton4() {
//		if (jButton4 == null) {
//			jButton4 = new JButton();
//			jButton4.setText("执行");
//		}
//		return jButton4;
//	}
//
//	private JRadioButton getJRadioButton3() {
//		if (jRadioButton3 == null) {
//			jRadioButton3 = new JRadioButton();
//			jRadioButton3.setSelected(true);
//		}
//		return jRadioButton3;
//	}
//
//	private JLabel getJLabel6() {
//		if (jLabel6 == null) {
//			jLabel6 = new JLabel();
//			jLabel6.setText("删除:");
//		}
//		return jLabel6;
//	}
//
//	private JRadioButton getJRadioButton2() {
//		if (jRadioButton2 == null) {
//			jRadioButton2 = new JRadioButton();
//			jRadioButton2.setSelected(true);
//		}
//		return jRadioButton2;
//	}
//
//	private JLabel getJLabel5() {
//		if (jLabel5 == null) {
//			jLabel5 = new JLabel();
//			jLabel5.setText("生成:");
//		}
//		return jLabel5;
//	}
//
//	private JPanel getJPanel0() {
//		if (jPanel0 == null) {
//			jPanel0 = new JPanel();
//			jPanel0.setLayout(new GroupLayout());
//			jPanel0.add(getJLabel5(), new Constraints(new Leading(40, 10, 10), new Leading(21, 10, 10)));
//			jPanel0.add(getJButton4(), new Constraints(new Leading(188, 10, 10), new Leading(14, 12, 12)));
//			jPanel0.add(getJLabel6(), new Constraints(new Leading(105, 10, 10), new Leading(21, 12, 12)));
//			jPanel0.add(getJRadioButton3(), new Constraints(new Leading(138, 10, 10), new Leading(18, 8, 8)));
//			jPanel0.add(getJRadioButton2(), new Constraints(new Leading(74, 10, 10), new Leading(18, 8, 8)));
//		}
//		return jPanel0;
//	}
//
//	private JRadioButton getJRadioButton0() {
//		if (jRadioButton0 == null) {
//			jRadioButton0 = new JRadioButton();
//			jRadioButton0.setSelected(true);
//		}
//		return jRadioButton0;
//	}
//
//	private JButton getJButton2() {
//		if (jButton2 == null) {
//			jButton2 = new JButton();
//			jButton2.setText("请选择");
//			jButton2.addMouseListener(new MouseAdapter() {
//				public void mouseClicked(MouseEvent event) {
//					jButton2MouseMouseClicked(event);
//				}
//			});
//		}
//		return jButton2;
//	}
//
//	private JButton getJButton1() {
//		if (jButton1 == null) {
//			jButton1 = new JButton();
//			jButton1.setText("请选择");
//			jButton1.addMouseListener(new MouseAdapter() {
//	
//				public void mouseClicked(MouseEvent event) {
//					jButton1MouseMouseClicked(event);
//				}
//			});
//		}
//		return jButton1;
//	}
//
//
//
//	private JButton getJButton0() {
//		if (jButton0 == null) {
//			jButton0 = new JButton();
//			jButton0.setText("请选择");
//			jButton0.addMouseListener(new MouseAdapter() {
//	
//				public void mouseClicked(MouseEvent event) {
//					jButton0MouseMouseClicked(event);
//				}
//			});
//		}
//		return jButton0;
//	}
//
//	
//
//	private JLabel getJLabel2() {
//		if (jLabel2 == null) {
//			jLabel2 = new JLabel();
//			jLabel2.setText("Web工程目录:");
//		}
//		return jLabel2;
//	}
//
//	private JLabel getJLabel1() {
//		if (jLabel1 == null) {
//			jLabel1 = new JLabel();
//			jLabel1.setText("Dal工程目录:");
//		}
//		return jLabel1;
//	}
//
//	private JLabel getJLabel0() {
//		if (jLabel0 == null) {
//			jLabel0 = new JLabel();
//			jLabel0.setText("Biz工程目录:");
//		}
//		return jLabel0;
//	}
//
//	private JScrollPane getJScrollPane0() {
//		if (jScrollPane0 == null) {
//			jScrollPane0 = new JScrollPane();
//			jScrollPane0.setViewportView(getJTree0());
//		}
//		return jScrollPane0;
//	}
//
//	private JTree getJTree0() {
//		if (jTree0 == null) {
//			jTree0 = new JTree();
//			jTree0.setBorder(BorderFactory.createEtchedBorder(
//					EtchedBorder.LOWERED, null, null));
//			DefaultTreeModel treeModel = null;
//			{
//				DefaultMutableTreeNode node0 = new DefaultMutableTreeNode(
//						"CodeGen");
//				node0.add(configNode);
//				node0.add(runNode);
//				treeModel = new DefaultTreeModel(node0);
//			}
//			jTree0.setModel(treeModel);
//			jTree0.addMouseListener(new MouseAdapter() {
//
//				public void mouseClicked(MouseEvent event) {
//					jTree0MouseMouseClicked(event);
//				}
//			});
//		}
//		return jTree0;
//	}
//
//	private static void installLnF() {
//		try {
//			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
//			if (lnfClassname == null)
//				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
//			UIManager.setLookAndFeel(lnfClassname);
//		} catch (Exception e) {
//			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
//					+ " on this platform:" + e.getMessage());
//		}
//	}
//
//	/**
//	 * Main entry of the class. Note: This class is only created so that you can
//	 * easily preview the result at runtime. It is not expected to be managed by
//	 * the designer. You can modify it as you like.
//	 */
//	public static void main(String[] args) {
//		installLnF();
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				DAOGenView frame = new DAOGenView();
//				frame.setDefaultCloseOperation(DAOGenView.EXIT_ON_CLOSE);
//				frame.setTitle("DAOGenView");
//				frame.getContentPane().setPreferredSize(frame.getSize());
//				frame.pack();
//				frame.setLocationRelativeTo(null);
//				frame.setVisible(true);
//			}
//		});
//	}
//
//	private void mouseMouseClicked(MouseEvent event) {
//	}
//
//	private void jTree0MouseMouseClicked(MouseEvent event) {
//		JTree tree = (JTree) event.getSource();
//		TreeSelectionModel modul = tree.getSelectionModel();
//		TreePath path = modul.getSelectionPath();
//		if (path != null) {
//			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
//					.getLastPathComponent();
//			if (node == configNode) {
//				showConfigurationDialog();
//			} else if (node == runNode) {
//				showRunDialog();
//			}
//		}
//	}
//	
//	private void showConfigurationDialog() {
//		getJPanel0().removeAll();
//		jPanel0.add(getJLabel0(), new Constraints(new Leading(40, 10, 10), new Leading(21, 10, 10)));
//		jPanel0.add(getJLabel1(), new Constraints(new Leading(40, 12, 12), new Leading(54, 12, 12)));
//		jPanel0.add(getJLabel2(), new Constraints(new Leading(40, 12, 12), new Leading(92, 10, 10)));
//		jPanel0.add(getJButton0(), new Constraints(new Leading(458, 10, 10), new Leading(16, 12, 12)));
//		jPanel0.add(getJButton1(), new Constraints(new Leading(458, 12, 12), new Leading(53, 12, 12)));
//		jPanel0.add(getJButton2(), new Constraints(new Leading(458, 12, 12), new Leading(87, 12, 12)));
//		jPanel0.add(getJTextField0(), new Constraints(new Leading(120, 306, 10, 10), new Leading(18, 12, 12)));
//		jPanel0.add(getJTextField1(), new Constraints(new Leading(120, 306, 12, 12), new Leading(54, 12, 12)));
//		jPanel0.add(getJTextField2(), new Constraints(new Leading(120, 306, 12, 12), new Leading(90, 12, 12)));
//		jPanel0.repaint();
//		jPanel0.setVisible(true);
//		super.setVisible(true);
//	}
//	
//	private void showRunDialog() {
//		getJPanel0().removeAll();
//		buttonGroup = new ButtonGroup();
//		buttonGroup.add(getJRadioButton2());
//		buttonGroup.add(getJRadioButton3());
//		jPanel0.add(getJLabel5(), new Constraints(new Leading(40, 10, 10), new Leading(21, 10, 10)));
//		jPanel0.add(getJRadioButton2(), new Constraints(new Leading(62, 10, 10), new Leading(16, 10, 10)));
//		jPanel0.add(getJLabel6(), new Constraints(new Leading(98, 10, 10), new Leading(21, 10, 10)));
//		jPanel0.add(getJRadioButton3(), new Constraints(new Leading(129, 10, 10), new Leading(18, 10, 10)));
//		jPanel0.add(getJButton4(), new Constraints(new Leading(168, 10, 10), new Leading(13, 10, 10)));
//		jPanel0.repaint();
//		jPanel0.setVisible(true);
//		super.setVisible(true);
//	}
//
//
//
//	private File file0 = null;
//	private File file1 = null;
//	private File file2 = null;
//	private JRadioButton jRadioButton0;
//	private JPanel jPanel0;
//	private JLabel jLabel5;
//	private JRadioButton jRadioButton2;
//	private JLabel jLabel6;
//	private JRadioButton jRadioButton3;
//	private JButton jButton4;
//	private ButtonGroup buttonGroup;
//	
//	private static final String PREFERRED_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
//	private void jButton0MouseMouseClicked(MouseEvent event) {
//		JFileChooser c = getJFileChooser0();
//		c.setDialogTitle("请选择");
//		int result  = c.showOpenDialog(getJPanel0());
//		if (result == JFileChooser.APPROVE_OPTION) {
//			file0 = c.getSelectedFile();
//			getJTextField0().setText(file0.getAbsolutePath());
//		}
//	}
//	
//	private void jButton1MouseMouseClicked(MouseEvent event) {
//		JFileChooser c = getJFileChooser1();
//		c.setDialogTitle("请选择");
//		int result  = c.showOpenDialog(getJPanel0());
//		if (result == JFileChooser.APPROVE_OPTION) {
//			file1 = c.getSelectedFile();
//			getJTextField1().setText(file1.getAbsolutePath());
//		}
//	}
//
//	private void jButton2MouseMouseClicked(MouseEvent event) {
//		JFileChooser c = getJFileChooser2();
//		c.setDialogTitle("请选择");
//		int result  = c.showOpenDialog(getJPanel0());
//		if (result == JFileChooser.APPROVE_OPTION) {
//			file2 = c.getSelectedFile();
//			getJTextField2().setText(file2.getAbsolutePath());
//		}
//	}
//	
//	private JFileChooser getJFileChooser0() {
//		if (jFileChooser0 == null) {
//			jFileChooser0 = new JFileChooser();
//			jFileChooser0.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		}
//		return jFileChooser0;
//	}
//
//	private JFileChooser getJFileChooser1() {
//		if (jFileChooser1 == null) {
//			jFileChooser1 = new JFileChooser();
//			jFileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		}
//		return jFileChooser1;
//	}
//	
//	private JFileChooser getJFileChooser2() {
//		if (jFileChooser2 == null) {
//			jFileChooser2 = new JFileChooser();
//			jFileChooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		}
//		return jFileChooser2;
//	}
//	
//	
//	private JTextField getJTextField0() {
//		if (jTextField0 == null) {
//			jTextField0 = new JTextField();
//			jTextField0.setText("");
//		}
//		return jTextField0;
//	}
//	
//	private JTextField getJTextField1() {
//		if (jTextField1 == null) {
//			jTextField1 = new JTextField();
//			jTextField1.setText("");
//		}
//		return jTextField1;
//	}
//	
//	private JTextField getJTextField2() {
//		if (jTextField2 == null) {
//			jTextField2 = new JTextField();
//			jTextField2.setText("");
//		}
//		return jTextField2;
//	}
//}
