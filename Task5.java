import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

class Student {
    protected String name, regNo, grade, place;
    Student() {
    }
    @Override
    public String toString() {
        return name + "\t" + regNo + "\t" + grade + "\t" + place + "\r\n";
    }
}

class DataManipulation {
     static void writeData(String[] row) {
        try {
            FileWriter fw = new FileWriter("StudentData.csv", true);
            fw.write(row[0] + "," + row[1] + "," + row[2] + "," + row[3] + ","+"0\r\n"); // 0 - not deleted
            fw.close();
        } catch (Exception ae) {
            System.out.println(ae);
        }

    }
    static boolean isUniqueRegister(String reg){
        for(Object[] row:getData()){
            if(row[1].equals(reg)) return false;
        }
        return true;
    }
    // convert the StudentData.csv records into Object array
    static Object[][] getData() {
        Object[][] data;
        ArrayList<Object[]> list = new ArrayList<>();
        try {
            File file = new File("StudentData.csv");
            if (file.exists()) {
                FileReader fr = new FileReader(file);
                char[] buff = new char[(int)file.length()];
                fr.read(buff);
                String[] rowData = new String(buff, 0, buff.length).split("\r\n");
                int j=0;
                for(int i=0;i<rowData.length;i++){
                    String[] columns = rowData[i].split(","); // Single Row data

                    if(columns[4].equals("0")){ // Ignore deleted record

                    list.add(new Object[]{columns[0], columns[1], columns[2], columns[3]});
                    }
                }
                fr.close();
                data = new Object[list.size()][4];
                for(int i=0;i<list.size();i++){
                    data[i] = list.get(i);
                }
                return data;
            } else {
                System.out.println("Please put StudentData.csv in the same folder");
                return null;
            }

        } catch (Exception e) {
            return null;
        }}
        static void removeStudent(String reg){
            try (RandomAccessFile file = new RandomAccessFile("StudentData.csv", "rw");){
                String line;
                while((line=file.readLine()) != null){
                    if(line.split(",")[1].equals(reg)){
                        file.seek(file.getFilePointer()-3);
                        file.write('1');
                        break;
                    }

                }
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

    static Object[] searchData(String regno){
        for(Object[] rows:getData()){
            if(rows[1].equals(regno)){ // not deleted record
                return new Object[]{rows[0],rows[1],rows[2],rows[3]};
            }
        }
        return null;
    }
}

class StudentManagementSystem{
    JFrame mainFrame,addStudentFrame,removeStudentFrame,searchFrame,displayFrame,updateStudentFrame;
    Font font = new Font("Serif",Font.BOLD,20);
    void mainPage(){
        JButton displayButton,addStudentButton,removeStudentButton,searchStudentButton,updateStudentButton;
        mainFrame = new JFrame("Main");
        JLabel head = new JLabel("Student Management System") ;
        head.setBounds(150,0,300,50);
        head.setFont(font);
        mainFrame.add(head);
        displayButton = new JButton("Display all student");
        displayButton.setBounds(100,100,200,50);
        displayButton.setFont(font);
        displayButton.addActionListener((ActionEvent ae)->{
            mainFrame.dispose();
            displayPage();
        });
        addStudentButton = new JButton("Add student");
        addStudentButton.setBounds(100,200,200,50);
        addStudentButton.setFont(font);
        addStudentButton.addActionListener((ActionEvent ae)->{
            mainFrame.setVisible(false);
            addStudentPage();
        });
        removeStudentButton = new JButton("Remove Student");
        removeStudentButton.setBounds(100,300,200,50);
        removeStudentButton.setFont(font);
        removeStudentButton.addActionListener((ActionEvent ae)->{
            mainFrame.setVisible(false);
            removeStudentPage();
        });
        searchStudentButton = new JButton("Search Student");
        searchStudentButton.setBounds(100,400,200,50);
        searchStudentButton.setFont(font);
        searchStudentButton.addActionListener((ActionEvent ae)->{
            mainFrame.setVisible(false);
            searchPage();
        });
        updateStudentButton = new JButton("Update Student");
        updateStudentButton.setBounds(100,500,200,50);
        updateStudentButton.setFont(font);
        updateStudentButton.addActionListener((ActionEvent ae)->{
            mainFrame.setVisible(false);
            updateStudentPage();
        });
        mainFrame.add(addStudentButton);mainFrame.add(removeStudentButton);mainFrame.add(displayButton);mainFrame.add(searchStudentButton);mainFrame.add(updateStudentButton);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600,700);

    }
    void addStudentPage(){
        JLabel l1,l2,l3,l4;
        JTextField name,reg,place;
        JButton enter,exit;
        addStudentFrame = new JFrame("Add Student");

        l1 = new JLabel("Student name");
        l1.setBounds(100,100,200,50);
        name = new JTextField();
        name.setBounds(250,100,200,50);
        l2 = new JLabel("Register no");
        l2.setBounds(100,200,200,50);
        reg = new JTextField();
        reg.setBounds(250,200,200,50);
        l3 = new JLabel("Grade");
        l3.setBounds(100,300,200,50);
        JComboBox<String> grade = new JComboBox<>();
        grade.addItem("A");grade.addItem("B");grade.addItem("C");grade.addItem("D");grade.addItem("E");
        grade.setBounds(250,300,100,50);
        l4 = new JLabel("Location");
        l4.setBounds(100,400,200,50);
        place = new JTextField();
        place.setBounds(250,400,200,50);
        enter = new JButton("Submit");
        enter.setBounds(200,500,100,40);
        enter.addActionListener((ActionEvent ae)->{
            if(DataManipulation.isUniqueRegister(reg.getText())){
                DataManipulation.writeData(new String[]{name.getText(),reg.getText(),(String)grade.getSelectedItem(),place.getText()});
                JOptionPane.showMessageDialog(null,"Successfully inserted","Title",JOptionPane.INFORMATION_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(null,"Register Number already exist","Title",JOptionPane.ERROR_MESSAGE);

            name.setText("");reg.setText("");place.setText("");

        });
        exit = new JButton("Exit");
        exit.setBounds(200,550,100,40);
        exit.addActionListener(al->{
            addStudentFrame.setVisible(false);
            mainPage();
        });
        addStudentFrame.add(l1);addStudentFrame.add(name);addStudentFrame.add(l2);addStudentFrame.add(reg);addStudentFrame.add(l3);addStudentFrame.add(grade);addStudentFrame.add(l4);addStudentFrame.add(place);addStudentFrame.add(enter);addStudentFrame.add(exit);
        addStudentFrame.setLayout(null);
        addStudentFrame.setSize(600,800);
        addStudentFrame.setVisible(true);
        addStudentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    void removeStudentPage(){
        JLabel l1;
        JTextField reg; JButton delete,exit;
        removeStudentFrame = new JFrame("Delete Student");
        l1 = new JLabel("Register No");
        l1.setBounds(100,100,200,50);
        reg = new JTextField();
        reg.setBounds(250,100,200,50);
        delete = new JButton("Delete");
        delete.setBounds(200,200,100,30);
        exit = new JButton();
        delete.addActionListener((ActionEvent e)->{
            Object[] data = DataManipulation.searchData(reg.getText());
            if(data == null)
            JOptionPane.showMessageDialog(null,"No Student found","Title",JOptionPane.ERROR_MESSAGE);
            else{
                int opt = JOptionPane.showConfirmDialog(null, "Are you sure want to Delete\nName: "+data[0]+"\nReg: "+data[1]+"\nGrade: "+data[2]+"\nLocation: "+data[3], "Confirmation", JOptionPane.YES_NO_OPTION);
                if(opt == 0){
                    DataManipulation.removeStudent(reg.getText());
                }
                else{reg.setText("");}
            reg.setText("");
            }
        });
        exit = new JButton("Exit");
        exit.setBounds(200,300,100,30);
        exit.addActionListener(ae->{
            removeStudentFrame.setVisible(false);
            mainPage();
        });
        removeStudentFrame.add(l1);removeStudentFrame.add(reg);removeStudentFrame.add(delete);removeStudentFrame.add(exit);
        removeStudentFrame.setSize(500,500);
        removeStudentFrame.setLayout(null);
        removeStudentFrame.setVisible(true);
        removeStudentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    void searchPage(){
        JLabel l1;  JTextField reg; JButton search,exit;
        searchFrame = new JFrame("Search Student");
        l1 = new JLabel("Register no");
        l1.setBounds(100,100,200,50);
        reg = new JTextField();
        reg.setBounds(300,100,200,40);
        search = new JButton("Search");
        search.setBounds(250,200,100,30);

        JPanel panel = new JPanel();
        //panel.setLayout(new GridBagLayout());
        String[] columnNames = {"Name", "Register No", "Grade","Location"};
        JTable searchResult = new JTable(new Object[][]{{"","","",""}},columnNames);
        searchResult.setFont(new Font("Calibri",Font.PLAIN,20));
        searchResult.setGridColor(Color.BLACK);
        searchResult.setIntercellSpacing(new Dimension(30,5));
        //table.setShowGrid(false);
        searchResult.setRowHeight(30);
        searchResult.setEnabled(false);
        // Size of table
        //searchResult.setPreferredSize(new Dimension(800,200));
        JScrollPane scrollPane = new JScrollPane(searchResult);
        panel.add(scrollPane);
        panel.setBounds(50,300,500,100);
        exit = new JButton("Exit");
        exit.setBounds(250,450,100,30);
        exit.addActionListener(ae->{
            searchFrame.setVisible(false);
            mainPage();
        });
        search.addActionListener((ActionEvent e)->{
            Object[] data = DataManipulation.searchData(reg.getText());
            if(data == null){
                JOptionPane.showMessageDialog(null,"No Student found","Title",JOptionPane.ERROR_MESSAGE);
                //Clear the previous data(if any)
                searchResult.setValueAt("",0,0);
                searchResult.setValueAt("",0,1);
                searchResult.setValueAt("",0,2);
                searchResult.setValueAt("",0,3);
            }
            else{
                searchResult.setValueAt(data[0],0,0);
                searchResult.setValueAt(data[1],0,1);
                searchResult.setValueAt(data[2],0,2);
                searchResult.setValueAt(data[3],0,3);
            }
        });
        searchFrame.add(l1);searchFrame.add(reg);searchFrame.add(search);searchFrame.add(panel);searchFrame.add(exit);
        searchFrame.setLayout(null);
        searchFrame.setVisible(true);
        searchFrame.setSize(700,700);
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
     void displayPage() {
        displayFrame = new JFrame("Display details");
        String[] columnNames = {"Name", "Register No", "Grade", "Location"};
        JTable table = new JTable(DataManipulation.getData(), columnNames);
        table.setFont(new Font("Calibri", Font.PLAIN, 20));
        table.setGridColor(Color.BLACK);
        table.setIntercellSpacing(new Dimension(30, 5));
        table.setRowHeight(30);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Calibri", Font.PLAIN, 20));
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayFrame.dispose();
                mainPage();
            }
        });

        // Add the button to the bottom of the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exitButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        // Add the panel to the frame
        displayFrame.add(panel);
        displayFrame.setSize(700, 700);
        displayFrame.setVisible(true);
        displayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    void updateStudentPage(){
        JLabel l1,l2,l3,l4,l5;JTextField reg,name,place;JButton search,update,exit,clear;

        updateStudentFrame = new JFrame("Update Student");
        l1 = new JLabel("Register no");
        l1.setBounds(100,50,150,40);
        reg = new JTextField();
        reg.setBounds(250,50,200,40);
        search = new JButton("Search");
        search.setBounds(250,150,100,30);
        l5 = new JLabel("Update here");
        l5.setBounds(300,220,150,30);
        l2 = new JLabel("Student name");
        l2.setBounds(100,300,150,40);
        name = new JTextField();
        name.setBounds(250,300,200,40);
        l3 = new JLabel("Grade");
        l3.setBounds(100,400,150,40);
        JComboBox<String> grade = new JComboBox<>();
        grade.addItem("A");grade.addItem("B");grade.addItem("C");grade.addItem("D");grade.addItem("E");
        grade.setBounds(250,400,100,40);
        l4 = new JLabel("Place");
        l4.setBounds(100,500,150,40);
        place= new JTextField();
        place.setBounds(250,500,200,40);
        update = new JButton("Update");
        update.setBounds(200,600,100,30);
        clear = new JButton("Clear");
        clear.setBounds(350,600,100,30);
        exit = new JButton("Exit");
        exit.setBounds(400,150,100,30);
        exit.addActionListener(ae->{
            updateStudentFrame.dispose();
            mainPage();
        });
        search.addActionListener(ae->{
        Object[] data= DataManipulation.searchData(reg.getText());
        if(data == null){
            JOptionPane.showMessageDialog(null,"Register Number doesn't exist","Title",JOptionPane.ERROR_MESSAGE);
        }
        else{

            name.setText((String)data[0]);
            grade.setSelectedItem(data[2]);
            place.setText((String)data[3]);
        }
        });
        update.addActionListener(ae->{
            DataManipulation.removeStudent(reg.getText());
            DataManipulation.writeData(new String[]{name.getText(),reg.getText(),(String)grade.getSelectedItem(),place.getText()});
            JOptionPane.showMessageDialog(null,"Updated Successfully","Title",JOptionPane.INFORMATION_MESSAGE);

        });
        clear.addActionListener(ae->{
            name.setText("");
            grade.setSelectedItem("A");
            place.setText("");
        });
        updateStudentFrame.add(l1);updateStudentFrame.add(reg);updateStudentFrame.add(search);updateStudentFrame.add(exit);updateStudentFrame.add(l5);
        updateStudentFrame.add(l2);updateStudentFrame.add(name);updateStudentFrame.add(l3);updateStudentFrame.add(grade);updateStudentFrame.add(l4);updateStudentFrame.add(place);updateStudentFrame.add(update);updateStudentFrame.add(clear);
        updateStudentFrame.setSize(700,700);
        updateStudentFrame.setLayout(null);
        updateStudentFrame.setVisible(true);
        updateStudentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

public class Task5 {
    public static void main(String[] args) {
        new StudentManagementSystem().mainPage();

    }
}
