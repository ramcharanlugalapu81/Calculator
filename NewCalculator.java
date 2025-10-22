import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
import javax.swing.*;

public class NewCalculator extends JFrame implements ActionListener {
    private JTextField textField;
    private JButton[] numberButtons = new JButton[10];
    private JButton addButton, subButton, mulButton, divButton, clearButton, eqButton, dotButton;
    private JPanel panel;

    public NewCalculator() {
        setTitle("Colorful Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        textField = new JTextField();
        textField.setBounds(50, 25, 300, 50);
        textField.setEditable(false);
        textField.setFont(new Font("Arial", Font.BOLD, 24));
        textField.setBackground(Color.CYAN);
        add(textField);

        addButton = createButton("+", Color.ORANGE);
        subButton = createButton("-", Color.ORANGE);
        mulButton = createButton("*", Color.ORANGE);
        divButton = createButton("/", Color.ORANGE);
        eqButton = createButton("=", Color.GREEN);
        clearButton = createButton("C", Color.RED);
        dotButton = createButton(".", Color.LIGHT_GRAY);

        for (int i = 0; i < 10; i++) {
            numberButtons[i] = createButton(String.valueOf(i), Color.PINK);
        }

        panel = new JPanel();
        panel.setBounds(50, 100, 300, 300);
        panel.setLayout(new GridLayout(4, 4, 10, 10));

        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(addButton);

        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);

        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(mulButton);

        panel.add(dotButton);
        panel.add(numberButtons[0]);
        panel.add(eqButton);
        panel.add(divButton);

        add(panel);

        clearButton.setBounds(50, 420, 300, 40);
        add(clearButton);

        setVisible(true);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 22));
        button.setBackground(color);
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 10; i++) {
            if (e.getSource() == numberButtons[i]) textField.setText(textField.getText() + i);
        }
        if (e.getSource() == dotButton) {
            String text = textField.getText();
            if (text.isEmpty() || endsWithOperator(text) || !lastNumberHasDot(text)) {
                textField.setText(text + ".");
            }
        }

        if (e.getSource() == addButton) appendOperator("+");
        if (e.getSource() == subButton) appendOperator("-");
        if (e.getSource() == mulButton) appendOperator("*");
        if (e.getSource() == divButton) appendOperator("/");

        if (e.getSource() == eqButton) {
            String expr = textField.getText();
            if (expr.isEmpty()) return;
            if (endsWithOperator(expr)) expr = expr.substring(0, expr.length() - 1);

            try {
                double result = evaluateExpression(expr);
                textField.setText(Double.toString(result));
            } catch (Exception ex) {
                textField.setText("Error");
            }
        }

        if (e.getSource() == clearButton) textField.setText("");
    }

    private boolean endsWithOperator(String text) {
        return text.endsWith("+") || text.endsWith("-") || text.endsWith("*") || text.endsWith("/");
    }

    private boolean lastNumberHasDot(String text) {
        String[] parts = text.split("[+\\-*/]");
        return parts[parts.length - 1].contains(".");
    }

    private void appendOperator(String op) {
        String text = textField.getText();
        if (text.isEmpty()) return;
        if (endsWithOperator(text)) textField.setText(text.substring(0, text.length() - 1) + op);
        else textField.setText(text + op);
    }

    // Evaluate simple arithmetic expression
    private double evaluateExpression(String expr) {
        // Using two stacks method (numbers and operators)
        Stack<Double> nums = new Stack<>();
        Stack<Character> ops = new Stack<>();
        StringBuilder num = new StringBuilder();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                num.append(c);
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                nums.push(Double.parseDouble(num.toString()));
                num.setLength(0);

                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(c)) {
                    double b = nums.pop();
                    double a = nums.pop();
                    nums.push(applyOp(ops.pop(), a, b));
                }

                ops.push(c);
            }
        }

        nums.push(Double.parseDouble(num.toString()));

        while (!ops.isEmpty()) {
            double b = nums.pop();
            double a = nums.pop();
            nums.push(applyOp(ops.pop(), a, b));
        }

        return nums.pop();
    }

    private int precedence(char op) {
        return (op == '+' || op == '-') ? 1 : 2;
    }

    private double applyOp(char op, double a, double b) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;
        }
        return 0;
    }

    public static void main(String[] args) {
        new NewCalculator();
    }
}
