import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class S_DESGUI extends JFrame {

    public S_DESGUI() {
        setTitle("S-des加解密系统");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        // 加解密模式面板
        JPanel encryptDecryptPanel = new JPanel();
        encryptDecryptPanel.setLayout(new GridLayout(5, 2));

        JTextField inputText = new JTextField();
        JTextField keyText = new JTextField();
        JTextArea outputText = new JTextArea();
        outputText.setEditable(false);

        String[] modes = { "Binary", "ASCII" };
        JComboBox<String> modeSelector = new JComboBox<>(modes);

        JButton encryptButton = new JButton("加密");
        JButton decryptButton = new JButton("解密");

        encryptDecryptPanel.add(new JLabel("明文/密文:"));
        encryptDecryptPanel.add(inputText);
        encryptDecryptPanel.add(new JLabel("密钥:"));
        encryptDecryptPanel.add(keyText);
        encryptDecryptPanel.add(new JLabel("选择模式:"));
        encryptDecryptPanel.add(modeSelector);
        encryptDecryptPanel.add(encryptButton);
        encryptDecryptPanel.add(decryptButton);
        encryptDecryptPanel.add(new JLabel("结果输出:"));
        encryptDecryptPanel.add(new JScrollPane(outputText));

        // 暴力破解模式面板
        JPanel bruteForcePanel = new JPanel();
        bruteForcePanel.setLayout(new GridLayout(6, 2));

        JTextField plainText = new JTextField();
        JTextField cipherText = new JTextField();
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        JButton crackButton = new JButton("破解");

        bruteForcePanel.add(new JLabel("明文:"));
        bruteForcePanel.add(plainText);
        bruteForcePanel.add(new JLabel("密文:"));
        bruteForcePanel.add(cipherText);
        bruteForcePanel.add(crackButton);
        bruteForcePanel.add(new JLabel("可能的密钥列表:"));
        bruteForcePanel.add(new JScrollPane(resultsArea));

        // 添加到主框架
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("加解密模式", encryptDecryptPanel);
        tabbedPane.addTab("暴力破解模式", bruteForcePanel);

        add(tabbedPane);


        // 添加按钮的事件监听器
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputText.getText();
                String key = keyText.getText();
                S_DES.getkey(key);
                StringBuilder result = new StringBuilder();
                if (modeSelector.getSelectedItem().equals("Binary")) {

                    result.append(S_DES.encipher(input));
                }
                else {
                    result.append(S_DES.ACSII_encipher(input));
                }

                outputText.setText(result.toString());
            }
        });


        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 解密逻辑待实现
                String input = inputText.getText();
                String key = keyText.getText();
                S_DES.getkey(key);
                StringBuilder result = new StringBuilder();
                boolean found = false;
                    if (modeSelector.getSelectedItem().equals("Binary")) {
                        result.append(S_DES.decode(input));

                    }
                    else {
                        result = S_DES.ACSII_decode(input);

                    }
                outputText.setText(result.toString());
            }
        });

        crackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String plain = plainText.getText();
                String cipher = cipherText.getText();
                resultsArea.append(S_DES.attack(plain,cipher).toString());
                bruteForcePanel.add(new JLabel("破解时间:"+S_DES.time));

            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            S_DESGUI frame = new S_DESGUI();
            frame.setVisible(true);
        });
    }
}