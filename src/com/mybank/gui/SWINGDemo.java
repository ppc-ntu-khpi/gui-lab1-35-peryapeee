package com.mybank.gui;

import com.mybank.data.DataSource;
import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexander 'Taurus' Babich
 */
public class SWINGDemo {
    
    private final JEditorPane log;
    private final JButton show;
    private final JButton report;
    private final JComboBox clients;
    
    public SWINGDemo() {
        log = new JEditorPane("text/html", "");
        log.setPreferredSize(new Dimension(250, 150));
        show = new JButton("Show");
        report = new JButton("Report");
        clients = new JComboBox();
        for (int i=0; i<Bank.getNumberOfCustomers();i++)
        {
            clients.addItem(Bank.getCustomer(i).getLastName()+", "+Bank.getCustomer(i).getFirstName());
        }
        
    }
    
    private void launchFrame() {
        JFrame frame = new JFrame("MyBank clients");
        frame.setLayout(new BorderLayout());
        JPanel cpane = new JPanel();
        cpane.setLayout(new GridLayout(1, 2));
        
        cpane.add(clients);
        cpane.add(show);
        cpane.add(report);
        frame.add(cpane, BorderLayout.NORTH);
        frame.add(log, BorderLayout.CENTER);
        frame.add(new JScrollPane(log), BorderLayout.CENTER);
        
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer current = Bank.getCustomer(clients.getSelectedIndex());
                String accType = current.getAccount(0)instanceof CheckingAccount?"Checking":"Savings";                
                String custInfo="<br>&nbsp;<b><span style=\"font-size:2em;\">"+current.getLastName()+", "+
                        current.getFirstName()+"</span><br><hr>"+
                        "&nbsp;<b>Acc Type: </b>"+accType+
                        "<br>&nbsp;<b>Balance: <span style=\"color:red;\">$"+current.getAccount(0).getBalance()+"</span></b>";
                
                log.setText(custInfo);                
            }
        });
        
        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder reportInfo = new StringBuilder();
                for(int cust = 0; cust < Bank.getNumberOfCustomers(); cust++){
                Customer current = Bank.getCustomer(cust);
                    String custInfo ="<br>&nbsp;<b><span style=\"font-size:2em;\">"+current.getLastName()+", "+
                            current.getFirstName()+"</span><br><hr>";
                    for(int acct = 0; acct < current.getNumberOfAccounts(); acct++){
                    String accType = current.getAccount(acct)instanceof CheckingAccount?"Checking":"Savings";                
                            custInfo += "&nbsp;<b>Acc Type: </b>"+accType+
                            "<br>&nbsp;<b>\t\tBalance: <span style=\"color:red;\">$"+current.getAccount(acct).getBalance()+"</span><br></b>";  
                    }
                    reportInfo.append(custInfo);
                }
                log.setText(reportInfo.toString());
            }
            
        });
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setResizable(false);
        frame.setVisible(true);        
    }
    
    public static void main(String[] args) {

        DataSource ds = new DataSource("./data/test.dat");
        try {
            ds.loadData();
        } catch (IOException ex) {
            Logger.getLogger(SWINGDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        SWINGDemo demo = new SWINGDemo();        
        demo.launchFrame();
    }
    
}