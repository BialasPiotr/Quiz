package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;


public class Quiz extends JFrame implements ActionListener {
    private JLabel questionLabel;
    private JButton answerButton1, answerButton2, answerButton3, answerButton4;
    private int currentQuestion = 0;
    private String[] questions;
    private String[][] answers;
    private String[] correctAnswers;
    private int score = 0;

    public Quiz() {
        setTitle("Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        questionLabel = new JLabel("Loading question...");
        add(questionLabel, BorderLayout.NORTH);

        JPanel answerPanel = new JPanel(new GridLayout(2, 2));
        answerButton1 = new JButton("Loading answer 1...");
        answerButton1.addActionListener(this);
        answerPanel.add(answerButton1);

        answerButton2 = new JButton("Loading answer 2...");
        answerButton2.addActionListener(this);
        answerPanel.add(answerButton2);

        answerButton3 = new JButton("Loading answer 3...");
        answerButton3.addActionListener(this);
        answerPanel.add(answerButton3);

        answerButton4 = new JButton("Loading answer 4...");
        answerButton4.addActionListener(this);
        answerPanel.add(answerButton4);

        add(answerPanel, BorderLayout.CENTER);

        setSize(400, 300);
        setVisible(true);

        try {
            URL url = new URL("https://opentdb.com/api.php?amount=10");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();
            in.close();

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            questions = new String[jsonArray.length()];
            answers = new String[jsonArray.length()][4];
            correctAnswers = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject questionObject = jsonArray.getJSONObject(i);
                questions[i] = questionObject.getString("question");

                JSONArray incorrectAnswersArray = questionObject.getJSONArray("incorrect_answers");
                for (int j = 0; j < incorrectAnswersArray.length(); j++) {
                    answers[i][j] = incorrectAnswersArray.getString(j);
                }

                answers[i][3] = questionObject.getString("correct_answer");
                correctAnswers[i] = questionObject.getString("correct_answer");
            }

            displayQuestion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == answerButton1) {
            checkAnswer(answerButton1.getText());
        } else if (e.getSource() == answerButton2) {
            checkAnswer(answerButton2.getText());
        } else if (e.getSource() == answerButton3) {
            checkAnswer(answerButton3.getText());
        } else if (e.getSource() == answerButton4) {
            checkAnswer(answerButton4.getText());
        }
    }

    private void displayQuestion() {
        questionLabel.setText(questions[currentQuestion]);
        answerButton1.setText(answers[currentQuestion][0]);
        answerButton2.setText(answers[currentQuestion][1]);
        answerButton3.setText(answers[currentQuestion][2]);
        answerButton4.setText(answers[currentQuestion][3]);
    }

    private void checkAnswer(String answer) {
        if (answer.equals(correctAnswers[currentQuestion])) {
            score++;
            JOptionPane.showMessageDialog(this, "Correct!");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect!");
        }

        currentQuestion++;

        if (currentQuestion == questions.length) {
            JOptionPane.showMessageDialog(this, "Quiz finished. Your score is " + score + " out of " + questions.length);
            System.exit(0);
        } else {
            displayQuestion();
        }
    }

    public static void main(String[] args) {
        new Quiz();

    }    }
