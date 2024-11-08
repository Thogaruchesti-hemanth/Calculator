package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private StringBuilder currentInput = new StringBuilder();
    private TextView textView;
    private ImageButton imageButton;
    private GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textViewDisplay);
        imageButton=findViewById(R.id.imageButton);
        gridLayout = findViewById(R.id.girdLayout2);



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (gridLayout.getVisibility() == View.VISIBLE) {
                    gridLayout.setVisibility(View.GONE);
                    adjustTextViewHeight(400);
                } else {
                    gridLayout.setVisibility(View.VISIBLE);
                    adjustTextViewHeight(250);
                }
            }
        });
    }

    private void adjustTextViewHeight(int dpValue) {
        ViewGroup.LayoutParams params = textView.getLayoutParams();
        float density = getResources().getDisplayMetrics().density;
        params.height = (int) (dpValue * density);
        textView.setLayoutParams(params);
    }


    public void onDigitClick(View view){

        Button button = (Button) view;
        currentInput.append(button.getText().toString());
        textView.setText(currentInput.toString());

    }


    public void onOperatorClick(View view){

        Button button= (Button) view;
        currentInput.append(button.getText().toString());
        textView.setText(currentInput.toString());
    }

    public void onSpecialOperatorClick(View view){
        Button button =(Button) view;
        String operator = button.getText().toString();
        calculateUnaryOperation(operator);
    }


    private void calculateUnaryOperation(String operator) {

        double result = 0;
        double operand1=0;

        try {
            operand1 = Double.parseDouble(currentInput.toString());
        } catch (NumberFormatException e) {
            textView.setText("Error");
            return;
        }
        switch (operator){
            case "sin":
                result = Math.sin(Math.toRadians(operand1));  // Convert degrees to radians
                break;
            case "cos":
                result = Math.cos(Math.toRadians(operand1));
                break;
            case "tan":
                result = Math.tan(Math.toRadians(operand1));
                break;
            case "log":
                result = Math.log(operand1);
                break;
            case "π":
                    result = Math.PI*operand1;
                break;
            case "e":
                result = Math.E*operand1;
                break;
            case "!":
                result = factorial((int) operand1);
                break;
        }

        textView.setText(String.valueOf(result));
        currentInput.setLength(0);
        currentInput.append(result); // Store result for further calculations
    }

    private long factorial(int n) {
        if (n == 0) return 1;
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }


    @SuppressLint("SetTextI18n")
    public void onEqualClick(View view) {

        try {
            String expression = currentInput.toString();
            double result = evaluateExpression(expression);
            textView.setText(String.valueOf(result));
            currentInput.setLength(0);
            currentInput.append(result); // Store result for further calculations
        } catch (Exception e) {
            textView.setText("Error");
            currentInput.setLength(0);
        }

    }

    private double evaluateExpression(String expression) {
        // Step 1: Perform multiplication and division
        expression = handleMultiplicationDivision(expression);

        // Step 2: Perform addition and subtraction
        return handleAdditionSubtraction(expression);
    }

    private String handleMultiplicationDivision(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int i = 0;
        while (i < expression.length()) {
            char currentChar = expression.charAt(i);

            if (Character.isDigit(currentChar) || currentChar == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i));
                    i++;
                }
                numbers.push(Double.parseDouble(num.toString()));
            } else if (currentChar == '*' || currentChar == '/') {
                char operator = currentChar;
                i++;
                double nextNum = 0;
                StringBuilder num = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i));
                    i++;
                }
                nextNum = Double.parseDouble(num.toString());

                if (operator == '*') {
                    numbers.push(numbers.pop() * nextNum);
                } else {
                    numbers.push(numbers.pop() / nextNum);
                }
            } else {
                i++;
            }
        }

        StringBuilder resultExpression = new StringBuilder();
        while (!numbers.isEmpty()) {
            resultExpression.insert(0, numbers.pop());
        }

        return resultExpression.toString();
    }

    private double handleAdditionSubtraction(String expression) {
        double result = 0;
        int i = 0;
        boolean isPositive = true;

        while (i < expression.length()) {
            char currentChar = expression.charAt(i);

            if (Character.isDigit(currentChar) || currentChar == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i));
                    i++;
                }
                if (isPositive) {
                    result += Double.parseDouble(num.toString());
                } else {
                    result -= Double.parseDouble(num.toString());
                }
            } else if (currentChar == '+' || currentChar == '-') {
                isPositive = currentChar == '+';
                i++;
            } else {
                i++;
            }
        }

        return result;
    }

    public void onClearClick(View view) {
        currentInput.setLength(0);
        textView.setText("0");
    }

}