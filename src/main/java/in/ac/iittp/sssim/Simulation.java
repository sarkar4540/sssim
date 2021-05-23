/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.ac.iittp.sssim;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sarkar4540
 */
public class Simulation extends javax.swing.JFrame {

    /**
     * Creates new form Simulation
     */
    private final int instructionQueueSize, storageSize, clockFrequency, loadBufferSize, storeBufferSize;
    final HashMap<String, Instruction> instructions;
    private final HashMap<String, Integer> rtSizes;
    private final SSSim sssim;
    final Register pc;
    final ArrayList<String> addresses;
    final HashMap<String, Register> registerFile;
    final HashMap<String, String> registerAddressTable;
    final ArrayDeque<Instruction> instructionQueue;
    final ArrayList<Instruction> loadBuffer, storeBuffer;
    final HashMap<String, JTable> rsTables;
    final HashMap<String, ArrayList<Instruction>> rs;
    HashMap<String, Object> cdb, l_cdb;
    final boolean ooo;

    public Simulation(String startAddress, int instructionQueueSize, int storageSize, int clockFrequency, int loadBufferSize, int storeBufferSize, HashMap<String, Instruction> instructions, HashMap<String, Integer> rtSizes, ArrayList<String> addresses, boolean ooo, SSSim sssim) {
        initComponents();
        this.instructionQueueSize = instructionQueueSize;
        this.storageSize = storageSize;
        this.clockFrequency = clockFrequency;
        this.loadBufferSize = loadBufferSize;
        this.storeBufferSize = storeBufferSize;
        this.instructions = instructions;
        this.rtSizes = rtSizes;
        this.sssim = sssim;
        this.registerFile = new HashMap<>();
        this.registerAddressTable = new HashMap<>();
        for (String reg_name : Register.integer_regs) {
            registerFile.put(reg_name, Register.fromName(reg_name));
            registerAddressTable.put(reg_name, null);
        }
        pc = registerFile.get("pc");
        pc.setValue(startAddress);
        for (String reg_name : Register.floating_point_regs) {
            registerFile.put(reg_name, Register.fromName(reg_name));
            registerAddressTable.put(reg_name, null);
        }
        this.instructionQueue = new ArrayDeque<>(instructionQueueSize);
        this.loadBuffer = new ArrayList<>(loadBufferSize);
        this.storeBuffer = new ArrayList<>(storeBufferSize);
        this.rsTables = new HashMap<>();
        this.rs = new HashMap<>();
        for (String op : rtSizes.keySet()) {
            JTable table = new JTable();
            rsTables.put(op, table);
            rs.put(op, new ArrayList<>(rtSizes.get(op)));
            jTabbedPane2.add(op, new JScrollPane(table));
        }
        this.insTime = 1.0 / clockFrequency;
        this.addresses = addresses;
        this.ooo = ooo;
        if (!ooo) {
            jTabbedPane1.setEnabledAt(0, false);
            jTabbedPane1.setEnabledAt(1, true);
            jTabbedPane1.setEnabledAt(2, false);
            jTabbedPane1.setEnabledAt(3, false);
            jTabbedPane1.setEnabledAt(4, false);
            jTabbedPane1.setEnabledAt(5, false);
            jTabbedPane1.setSelectedIndex(1);
        }
        this.cdb = new HashMap<>();
        setExtendedState(MAXIMIZED_BOTH);
        tick();
    }

    boolean endReached = false;
    Instruction current = null;

    private void tickInOrder() {
        Instruction i = null;

        while (i == null) {

            if (pc.getIntValue().intValueExact() > storageSize) {
                endReached = true;
                pause();
                return;
            }
            i = instructions.get(pc.getValue());
            pc.setValue(pc.getIntValue().add(BigInteger.TWO));

        }
        System.out.println(i);
        if (!i.hasValue()) {
            i.decode(this);

            time += insTime;
            if (i.ready) {
                current = i;
                i.execute(this);
                time += insTime;
                i.dispatch(this);
                time += insTime;
            } else {
                System.out.println("Error decode: " + i);
            }
        }

        time += insTime;
        updateUI();
    }

    private void tick() {

        if (!ooo) {
            tickInOrder();
            return;
        }

        for (Instruction i : loadBuffer) {
            if (i.ready) {
                loadBuffer.remove(i);
                System.out.println("Execute: " + i);
                i.execute(this);
                i.dispatch(this);
                System.out.println("Dispatch: " + i.getResult(this));
                i.RATUnset(this);
                break;
            }
        }

        for (Instruction i : storeBuffer) {
            if (i.ready) {
                storeBuffer.remove(i);
                System.out.println("Execute: " + i);
                i.execute(this);
                break;
            }
        }

        for (String rs_name : rs.keySet()) {
            ArrayList<Instruction> rStat = rs.get(rs_name);

            for (Instruction i : rStat) {
                if (i.ready) {
                    rStat.remove(i);
                    System.out.println("Execute: " + i);
                    i.execute(this);
                    i.dispatch(this);
                    System.out.println("Dispatch: " + i.getResult(this));
                    i.RATUnset(this);
                    break;
                }
            }
        }

        loadBuffer.forEach(i -> {
            i.decode(this);
            System.out.println("Held: " + i);
        });

        storeBuffer.forEach(i -> {
            i.decode(this);
            System.out.println("Held: " + i);
        });

        for (String rs_name : rs.keySet()) {
            ArrayList<Instruction> rStat = rs.get(rs_name);

            rStat.forEach(i -> {
                i.decode(this);
                System.out.println("Held: " + i);
            });
        }

        if (!instructionQueue.isEmpty()) {
            Instruction i = instructionQueue.peekLast();
            System.out.println("Decode: " + i);
            switch (i.getType()) {
                case Instruction.ARITHMETIC_OPS:
                case Instruction.BITWISE_OPS:
                    String op = i.getOpcode();
                    if (rs.containsKey(op) && rs.get(op).size() < rtSizes.get(op)) {
                        i.decode(this);
                        i.RATSet(this);
                        rs.get(op).add(instructionQueue.removeLast());
                    }
                    break;
                case Instruction.LOAD_OPS:
                    if (loadBuffer.size() < loadBufferSize) {
                        i.decode(this);
                        i.RATSet(this);
                        loadBuffer.add(instructionQueue.removeLast());
                    }
                    break;
                case Instruction.STORE_OPS:
                    if (storeBuffer.size() < storeBufferSize) {
                        i.decode(this);
                        storeBuffer.add(instructionQueue.removeLast());
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Invalid Instruction: " + instructionQueue.removeLast(), "Decode Error", JOptionPane.ERROR_MESSAGE);
                    pause();
                    break;
            }

        }

        //Loading Instruction Queue
        while (instructionQueue.size() < instructionQueueSize && !endReached) {

            Instruction i = null;

            while (i == null) {

                if (pc.getIntValue().intValueExact() > storageSize) {
                    endReached = true;
                    pause();
                    return;
                }
                i = instructions.get(pc.getValue());
                pc.setValue(pc.getIntValue().add(BigInteger.TWO));

            }
            System.out.println("Fetch: " + i);
            if (!i.hasValue()) {
                if (i.getType() == Instruction.CONTROL_TRANSFER_OPS) {
                    time += insTime;
                    System.out.println("End cycle: " + time);
                    if (instructionQueue.isEmpty()) {
                        System.out.println("Decode: " + i);
                        i.decode(this);
                        time += insTime;
                        System.out.println("End cycle: " + time);
                        if (i.ready) {

                            System.out.println("Execute: " + i);
                            i.execute(this);
                            break;
                        }
                    } else {
                        System.out.println("Held: " + i);
                        pc.setValue(pc.getIntValue().subtract(BigInteger.TWO));
                        break;
                    }
                } else {
                    instructionQueue.addFirst(Instruction.fromInstruction(i));
                }

            }

        }
        time += insTime;
        System.out.println("End cycle: " + time);
        updateUI();
    }

    double time = 0, insTime;

    private void updateUI() {
        jLabel1.setText("Program Counter: " + pc.getValue() + ", Time(sec): " + time + (current != null ? ", Executed: " + current : ""));
        if (ooo) {
            jTable1.setModel(instructionTableModel(instructionQueue));
            jTable3.setModel(registerAddressTableModel(registerAddressTable));
            jTable4.setModel(instructionTableModel(loadBuffer));
            jTable5.setModel(instructionTableModel(storeBuffer));
            for (String op : rs.keySet()) {
                rsTables.get(op).setModel(reservationStationModel(rs.get(op)));
            }
        }
        jTable2.setModel(registerFileTableModel(registerFile));
    }

    private DefaultTableModel reservationStationModel(ArrayList<Instruction> station) {
        Vector<Vector<String>> data = new Vector<>();
        Vector<String> columns = new Vector<>();
        columns.add("index");
        columns.add("opcode");
        columns.add("rd");
        columns.add("rs1");
        columns.add("rs2");
        columns.add("rs3");
        for (int c = 0; c < station.size(); c++) {
            Instruction i = station.get(c);
            Vector<String> entry = new Vector<>();
            entry.add(i.getAddress());
            entry.add(i.getOpcode());
            for (String operand : i.getOperands()) {
                entry.add(operand);
            }
            data.add(entry);
        }
        return new DefaultTableModel(data, columns);
    }

    private DefaultTableModel instructionTableModel(Collection<Instruction> queue) {
        Vector<Vector<String>> data = new Vector<>();
        Vector<String> columns = new Vector<>();
        columns.add("mem");
        columns.add("op-code");
        int maxcol = 0;
        for (Instruction i : queue) {
            Vector<String> entry = new Vector<>();
            entry.add(i.getAddress());
            entry.add(i.getOpcode());
            if (i.getOperands() != null) {
                for (String operand : i.getOperands()) {
                    entry.add(operand);
                }
                maxcol = Math.max(maxcol, i.getOperands().length);
            }
            data.add(entry);
        }
        for (int i = 1; i <= maxcol; i++) {
            columns.add("operand" + i);
        }
        return new DefaultTableModel(data, columns);
    }

    private DefaultTableModel registerFileTableModel(HashMap<String, Register> regFile) {
        Vector<Vector<String>> data = new Vector<>();
        Vector<String> columns = new Vector<>();
        columns.add("Register");
        columns.add("Type");
        columns.add("Value");
        for (Register r : regFile.values()) {
            Vector<String> entry = new Vector<>();
            entry.add(r.getName());
            entry.add(r.getTypeString());
            entry.add(r.getValue());
            data.add(entry);
        }
        return new DefaultTableModel(data, columns);

    }

    private DefaultTableModel registerAddressTableModel(HashMap<String, String> regAddressTable) {
        Vector<Vector<String>> data = new Vector<>();
        Vector<String> columns = new Vector<>();
        columns.add("reg");
        columns.add("addr");
        for (String r : regAddressTable.keySet()) {
            Vector<String> entry = new Vector<>();
            entry.add(r);
            entry.add(regAddressTable.get(r));
            data.add(entry);
        }
        return new DefaultTableModel(data, columns);

    }

    private boolean play = false;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSlider1 = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("RISC-V Simulator - Simulation");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jTabbedPane1.setMaximumSize(null);
        jTabbedPane1.setMinimumSize(null);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTabbedPane1.addTab("Instruction Queue", jScrollPane1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jTabbedPane1.addTab("Register File", jScrollPane2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jTabbedPane1.addTab("Register Address Table", jScrollPane3);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTable4);

        jTabbedPane1.addTab("Load Buffer", jScrollPane4);

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(jTable5);

        jTabbedPane1.addTab("Store Buffer", jScrollPane5);
        jTabbedPane1.addTab("Reservation Stations", jTabbedPane2);

        jLabel1.setText("jLabel1");

        jButton1.setText("Play");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Pause");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Next");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jSlider1.setMaximum(1000);
        jSlider1.setMinimum(1);
        jSlider1.setValue(500);

        jLabel2.setText("Delay (ms)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        play = false;
        sssim.setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jButton1.setEnabled(false);
        jButton2.setEnabled(true);
        jButton3.setEnabled(false);
        play = true;
        new Thread(() -> {
            try {
                while (play) {
                    tick();
                    Thread.sleep(jSlider1.getValue());
                }
            } catch (InterruptedException ex) {
                pause();
                Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }//GEN-LAST:event_jButton1ActionPerformed
    public void pause() {
        jButton1.setEnabled(true);
        jButton2.setEnabled(false);
        jButton3.setEnabled(true);
        play = false;
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        pause();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        tick();
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Simulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Simulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Simulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Simulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Simulation().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    // End of variables declaration//GEN-END:variables
}
