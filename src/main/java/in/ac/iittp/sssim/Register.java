/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.ac.iittp.sssim;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author sarkar4540
 */
public class Register {

    /**
     * @return the intval
     */
    public BigInteger getIntValue() {
        return intval;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        setValue(new BigInteger(value,16));
        System.out.println("Updated ["+name+"] value to:"+this.getValue());
    }
    public void setValue(Object value) {
        if(value instanceof BigInteger)setValue((BigInteger)value);
        else if(value instanceof BigDecimal)setValue((BigDecimal)value);
        else setValue(value.toString());
        System.out.println(getValue());
    }
    
    public void setValue(BigInteger value) {
        this.intval = value;
        this.fpval = new BigDecimal(value.toString(10));
        System.out.println("Updated ["+name+"] value to:"+this.getValue());
    }
    public void setValue(BigDecimal value) {
        this.intval = value.toBigInteger();
        this.fpval = value;
        System.out.println("Updated ["+name+"] value to:"+this.getValue());
    }
    
    public String getValue(){
        return type==INTEGER?intval.toString(16):type==FLOATING_POINT?fpval.toPlainString():"0";
    }

    /**
     * @return the fpval
     */
    public BigDecimal getFPValue() {
        return fpval;
    }
    public final static int INTEGER=1,FLOATING_POINT=2, UNKNOWN=0;
    public final static String[] integer_regs={
        "pc",
        "zero",
        "ra",
        "sp",
        "gp",
        "tp",
        "t0",
        "t1",
        "t2",
        "s0",
        "s1",
        "a0",
        "a1",
        "a2",
        "a3",
        "a4",
        "a5",
        "a6",
        "a7",
        "s2",
        "s3",
        "s4",
        "s5",
        "s6",
        "s7",
        "s8",
        "s9",
        "s10",
        "s11",
        "t3",
        "t4",
        "t5",
        "t6"
    },
            floating_point_regs={
                "ft0",
                "ft1",
                "ft2",
                "ft3",
                "ft4",
                "ft5",
                "ft6",
                "ft7",
                "fs0",
                "fs1",
                "fa0",
                "fa1",
                "fa2",
                "fa3",
                "fa4",
                "fa5",
                "fa6",
                "fa7",
                "fs2",
                "fs3",
                "fs4",
                "fs5",
                "fs6",
                "fs7",
                "fs8",
                "fs9",
                "fs10",
                "fs11",
                "ft8",
                "ft9",
                "ft10",
                "ft11"                    
            };
    
    private String name;
    private BigInteger intval;
    private BigDecimal fpval;
    public static Register fromName(String name){
        Register reg=new Register();
        reg.name=name;
        reg.inferType();
        reg.setValue("0");
        return reg;
    }
    private int type;
    private void inferType(){
        List<String> integer_regs_list=Arrays.asList(integer_regs),
                floating_point_regs_list=Arrays.asList(floating_point_regs);
        type=integer_regs_list.contains(name)?INTEGER:
                floating_point_regs_list.contains(name)?FLOATING_POINT:
                UNKNOWN;
        
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public int getType(){
        return type;
    }
    
    public boolean isValid(){
        return type==INTEGER||type==FLOATING_POINT;
    }

    String getTypeString() {
        return type==INTEGER?"Integer":type==FLOATING_POINT?"Floating Point":"Unknown";
    }
}
