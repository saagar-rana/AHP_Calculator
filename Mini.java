import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.table.*;

class SimpleFrame extends JFrame {

    private JTextField colsField;
    private JTable table;
	public static int widthFrame;
	public static int heightFrame;

    public SimpleFrame() {

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension d = kit.getScreenSize();

        int widthScreen = d.width;
        int heightScreen = d.height;
		
		widthFrame = widthScreen/2;
		heightFrame = heightScreen/2;

        setSize(widthScreen / 2, heightScreen / 2);
        setLocation(widthScreen / 4, heightScreen / 4);

        colsField = new JTextField(5);
        JButton createButton = new JButton("Create Table");
        JButton compute = new JButton("Compute");

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int cols = 0;
				
			
             cols = Integer.parseInt(colsField.getText());
       
				
                String[][] data = new String[cols][cols + 1];
                String[] columnNames = new String[cols + 1];
                columnNames[0] = " ";

                for (int i = 0; i < cols; i++) {

                    columnNames[i + 1] = JOptionPane.showInputDialog(null, "Enter the column name " + (i + 1), "Input",
                            JOptionPane.QUESTION_MESSAGE);
                    data[i][0] = columnNames[i + 1];
                    data[i][i + 1] = "1";
                }

                for (int i = 0; i < cols; i++) {
                    for (int j = i; j < cols; j++) {
                        if (data[i][j + 1] == "1") {
                            continue;
                        } else {
							boolean validInput = false;
                                
	while (!validInput) {
			try {
        data[i][j + 1] = JOptionPane.showInputDialog(null,
                "Compare " + data[i][0] + " with " + columnNames[j + 1], "Input",
                JOptionPane.QUESTION_MESSAGE);

        data[j][i + 1] = String.valueOf(1.0f / Float.parseFloat(data[i][j + 1]));
        validInput = true; // Break out of the loop if parsing and calculation are successful
		} catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Enter a valid value");
    }
}


                        }

                    }
                }

                table = new JTable(data, columnNames);
                JScrollPane scrollPane = new JScrollPane(table);
                Draw panel = new Draw(scrollPane);
                getContentPane().removeAll(); // Clear the existing components
                getContentPane().setLayout(new BorderLayout());
                getContentPane().add(panel, BorderLayout.CENTER);
                getContentPane().add(compute, BorderLayout.SOUTH);
                pack(); // Adjust the frame size to fit the components
                setLocationRelativeTo(null); // Center the frame on the screen
                revalidate();
                repaint();
            }

        });

        compute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                TableModel model = table.getModel();
                int rows = model.getRowCount();
                Float[] sum = new Float[rows];

                for (int i = 0; i < rows; i++) {

                    sum[i] = 0.0f;

                    for (int j = 0; j < rows; j++) {
                        Object value = model.getValueAt(j, i + 1);

                        if (value instanceof Float) {
                            sum[i] += (Float) value;
                        } else if (value instanceof String) {
                            try {
                                float intValue = Float.parseFloat((String) value);
                                sum[i] += intValue;
                            } catch (NumberFormatException ex) {
								System.out.print("please enter a number");
                            }
                        }

                    }

                }

                Float[][] divsum = new Float[rows][rows];
                for (int i = 0; i < divsum.length; i++) {
                    for (int j = 0; j < divsum.length; j++) {
                        Object value = model.getValueAt(j, i + 1);
                        divsum[j][i] = Float.parseFloat((String) value) / sum[i];

                    }

                }

                Float[] cweight = new Float[rows];
                for (int i = 0; i < cweight.length; i++) {
                    cweight[i] = 0.0f;
                    for (int j = 0; j < cweight.length; j++) {

                        cweight[i] += divsum[i][j];

                    }

                    cweight[i] /= cweight.length;

                }

                Float[][] check = new Float[rows][rows];
                for (int i = 0; i < check.length; i++) {
                    for (int j = 0; j < check.length; j++) {
                        Object value = model.getValueAt(j, i + 1);
                        check[j][i] = Float.parseFloat((String) value) * cweight[i];

                    }

                }

                Float[] wsum = new Float[rows];
                Float[] ratio = new Float[rows];
                for (int i = 0; i < wsum.length; i++) {
                    wsum[i] = 0.0f;
                    for (int j = 0; j < wsum.length; j++) {

                        wsum[i] += check[i][j];

                    }

                    ratio[i] = (Float) wsum[i] / (Float) cweight[i];

                }

                float lamdaMax = 0.0f;
                float consitencyIndex = 0.0f;

                for (int i = 0; i < rows; i++) {
                    lamdaMax += ratio[i] / 4;
                }

                consitencyIndex = (lamdaMax - rows) / (rows - 1);

                Float[] randomIndex = { 0.0f, 0.0f, 0.58f, 0.9f, 1.12f, 1.24f, 1.32f, 1.41f, 1.45f, 1.49f };

                float consistencyRatio = 0.0f;
                consistencyRatio = consitencyIndex / randomIndex[rows - 1];

                if (consistencyRatio < 0.1) {
                    JOptionPane.showMessageDialog(null,
                            "The consistency ratio is " + consistencyRatio + " which is less than 0.1");

                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < rows; i++) {
                        sb.append("Weightage for ").append(model.getValueAt(i, 0) + " ").append(cweight[i])
                                .append("\n");
                    }
                    JOptionPane.showMessageDialog(null, sb.toString());
                }

                else {
                    JOptionPane.showMessageDialog(null, "The consistency ratio is " + consistencyRatio
                            + " which is greater than 0.1. Please re-enter the values");
                }

            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter the number of fields: "));
        inputPanel.add(colsField);
        inputPanel.add(createButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPanel, BorderLayout.SOUTH);
		
		Text text = new Text();
		getContentPane().add(text);
		
		Table2 tpanel = new Table2();
		getContentPane().add(tpanel);
		
    }

   
}

    class Text extends JPanel{
		public Text(){
			//setBackground(Color.BLACK);
			setBounds(0,0,SimpleFrame.widthFrame,SimpleFrame.heightFrame/4);
			
		}
		public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Font f1 = new Font("Serif", Font.PLAIN, 50);
	Font f2 = new Font("Serif", Font.PLAIN, 15);
    g.setFont(f1);

    String text = "Analytical Heirarchy Process";
    int textWidth = g.getFontMetrics().stringWidth(text);
	int dy = g.getFontMetrics().getAscent();
    int x = (getWidth() - textWidth) / 2; // Calculate the x-coordinate

    int y = 50; // Adjust the y-coordinate as needed

    g.drawString(text, x, y);
	g.drawString("Calculator", (getWidth()-g.getFontMetrics().stringWidth("Calculator"))/2, y+dy);
	
	g.setFont(f2);
	g.drawString("dsafas",10,y+2*dy);
}

		private JButton button;
	}
	
	class Table2 extends JPanel{
		public Table2(){
			setBounds(0,0,SimpleFrame.widthFrame,SimpleFrame.heightFrame-SimpleFrame.heightFrame/4);
			setBackground(Color.BLACK);
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.setColor(Color.white);
            int y = 170;
            int dy = 25;
            Font f2 = new Font("Serif", Font.PLAIN, 20);
            g.setFont(f2);

            g.drawString("AHP is an approach to decision-making multi-objective multicriterion, which allows the user to arrive at a",30,y);
            y+=dy;
            g.drawString("arrive at a scale rather than pulling off a set of alternative solutions (Saaty, 1980).",30,y);
            y+=dy;
            g.drawString("This method is solved by developing a pairwise comparison matrix by assigning the rank to each factor ",30,y);
            y+=dy;
            g.drawString("against other factors.",30,y);
            y+=dy;
            y+=dy;
			g.drawString("Equal importance => 1",30,y);
            y+=dy;
            g.drawString("Moderately importance => 3",30,y);
             y+=dy;
            g.drawString("Strongly importance => 5",30,y);
            y+=dy;
            g.drawString("Very strongly importance => 7",30,y);
             y+=dy;
            g.drawString("Extremely importance => 9",30,y);
            y+=dy;
            g.drawString("Intermediate values => 2, 4, 6, and 8",30,y);
             
		}
	}
	
	


	class Draw extends JPanel {
	private JScrollPane scrollPane;

	public Draw(JScrollPane scrollPane) {
	this.scrollPane = scrollPane;
	add(this.scrollPane);
}
 }

public class Mini {
    public static void main(String[] args) {
        SimpleFrame frame = new SimpleFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}