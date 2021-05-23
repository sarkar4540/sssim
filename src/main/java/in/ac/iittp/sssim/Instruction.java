/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.ac.iittp.sssim;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author sarkar4540
 */
class Instruction {

    static Instruction fromInstruction(Instruction i) {
        Instruction i1 = new Instruction();
        i1.address = i.address;
        i1.opcode = i.opcode;
        i1.operands = Arrays.copyOf(i.operands, i.operands.length);
        i1.type = i.type;
        i1.value = i.value;
        return i1;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    public static final int UNKNOWN_OPS = 0, CONTROL_TRANSFER_OPS = 1,
            BITWISE_OPS = 2,
            ARITHMETIC_OPS = 3,
            LOAD_OPS = 4,
            STORE_OPS = 5,
            PSEUDO_OPS = 6;
    public static final String[] control_transfer_ops = {
        "beq",
        "bne",
        "blt",
        "bltu",
        "bge",
        "bgeu",
        "jal",
        "jalr",
        "ecall",
        "ebreak",
        "fence",
        "fence.i"
    },
            bitwise_ops = {
                "sll",
                "srl",
                "sra",
                "and",
                "or",
                "xor",
                "slt",
                "sltu",
                "slli",
                "srli",
                "srai",
                "andi",
                "ori",
                "xori",
                "slti",
                "sltiu",
                "sllw",
                "srlw",
                "sraw",
                "slliw",
                "srliw",
                "sraiw",
                "feq.s",
                "feq.d",
                "flt.s",
                "flt.d",
                "fle.s",
                "fle.d",
                "fclass.s",
                "fclass.d"
            },
            arithmetic_ops = {
                "add",
                "sub",
                "mul",
                "mulh",
                "mulhu",
                "mulhsu",
                "div",
                "divu",
                "rem",
                "addi",
                "addw",
                "subw",
                "remu",
                "mulw",
                "divw",
                "remw",
                "remuw",
                "lui",
                "auipc",
                "addiw",
                "fmv.w.x",
                "fmv.x.w",
                "fmv.d.x",
                "fmv.x.d",
                "fcvt.s.w",
                "fcvt.d.w",
                "fcvt.s.wu",
                "fcvt.w.s",
                "fcvt.w.d",
                "fcvt.wu.s",
                "fcvt.wu.d",
                "fcvt.d.s",
                "fcvt.s.d",
                "fcvt.s.l",
                "fcvt.d.l",
                "fcvt.s.lu",
                "fcvt.d.lu",
                "fcvt.l.s",
                "fcvt.l.d",
                "fcvt.lu.s",
                "fcvt.lu.d",
                "fadd.s",
                "fadd.d",
                "fadd.q",
                "fsub.s",
                "fsub.d",
                "fsub.q",
                "fmul.s",
                "fmul.d",
                "fmul.q",
                "fdiv.s",
                "fdiv.d",
                "fdiv.q",
                "fsqrt.s",
                "fsqrt.d",
                "fsqrt.q",
                "fmadd.s",
                "fmadd.d",
                "fmadd.q",
                "fmsub.s",
                "fmsub.d",
                "fmsub.q",
                "fnmsub.s",
                "fnmsub.d",
                "fnmsub.q",
                "fnmadd.s",
                "fnmadd.d",
                "fnmadd.q",
                "fsgnj.s",
                "fsgnj.d",
                "fsgnj.q",
                "fsgnjn.s",
                "fsgnjn.d",
                "fsgnjn.q",
                "fsgnjx.s",
                "fsgnjx.d",
                "fsgnjx.q",
                "fmin.s",
                "fmin.d",
                "fmin.q",
                "fmax.s",
                "fmax.d",
                "fmax.q"

            },
            store_ops = {
                "sb",
                "sh",
                "sw",
                "sd",
                "fsw",
                "fsd",
                "fsq"
            },
            load_ops = {
                "lb",
                "lbu",
                "lh",
                "lhu",
                "lw",
                "ld",
                "lwu",
                "flw",
                "fld",
                "flq"
            },
            pseudo_ops = {
                "nop",
                "c.nop",
                "unimp",
                "li",
                "mv",
                "la",
                "sext.w",
                "not",
                "neg",
                "negw",
                "seqz",
                "snez",
                "sltz",
                "sgtz",
                "beqz",
                "bnez",
                "blez",
                "bgez",
                "bltz",
                "bgtz",
                "bgt",
                "ble",
                "bgtu",
                "bleu",
                "ret",
                "j",
                "jr",
                "fmv.s",
                "fabs.s",
                "fneg.s",
                "fmv.d",
                "fabs.d",
                "fneg.d",
                "c.lw",
                "c.lwsp",
                "c.flw",
                "c.flwsp",
                "c.fld",
                "c.fldsp",
                "c.sw",
                "c.swsp",
                "c.fsw",
                "c.fswsp",
                "c.fsd",
                "c.fsdsp",
                "c.add",
                "c.addi",
                "c.addi16sp",
                "c.addi4spn",
                "c.sub",
                "c.and",
                "c.andi",
                "c.or",
                "c.xor",
                "c.mv",
                "c.li",
                "c.lui",
                "c.slli",
                "c.srai",
                "c.srli",
                "c.beqz",
                "c.bnez",
                "c.j",
                "c.jr",
                "c.jal",
                "c.jalr",
                "c.ebreak"
            };

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setProcedure(String value) {
        this.setValue(value);
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public static boolean isValidOperand(String operand) {
        if (operand.matches(".*\\([a-z]+[0-9]*\\)")) {
            operand = operand.split("[\\(\\)]")[1];
        }
        if (Register.fromName(operand).isValid()) {
            return true;
        } else {
            return operand.matches("-{0,1}[0-9a-f\\.]+");
        }
    }
    public boolean ready = false;

    public void decode(Simulation sim) {
        ready = true;
        for (int i = type == STORE_OPS ? 0 : 1; i < operands.length; i++) {
            String operand = operands[i];
            if (operand.matches(".*\\([a-z]+[0-9]*\\)")) {
                String[] parts = operand.split("[\\(\\)]");
                if (sim.registerFile.containsKey(parts[1])) {
                    if (sim.registerAddressTable.get(parts[1]) == null) {
                        switch (sim.registerFile.get(parts[1]).getType()) {
                            case Register.INTEGER:
                                operands[i] = sim.registerFile.get(parts[1]).getIntValue().add(toBigInteger(parts[0])).toString(16);
                                break;
                            case Register.FLOATING_POINT:
                                operands[i] = sim.registerFile.get(parts[1]).getFPValue().add(toBigDecimal(parts[0])).toPlainString();
                                break;
                            default:
                                System.err.println("Error decoding operand:" + operand);
                                break;
                        }
                    } else {
                        operands[i] = parts[0] + "(rs." + sim.registerAddressTable.get(parts[1]) + ")";
                        ready = false;
                    }
                }
            } else if (sim.registerFile.containsKey(operand)) {
                if (sim.registerAddressTable.get(operand) == null) {
                    operands[i] = sim.registerFile.get(operand).getValue();
                } else {
                    operands[i] = "rs." + sim.registerAddressTable.get(operand);
                    ready = false;
                }
            } else if (operand.matches(".*\\(rs\\..*\\)")) {
                String[] parts = operand.split("[\\(\\)]");
                if (sim.cdb.containsKey(parts[1])) {
                    Object l_val = sim.cdb.get(parts[1]);
                    if (l_val instanceof BigInteger) {
                        operands[i] = ((BigInteger) l_val).add(toBigInteger(parts[0])).toString(16);
                    } else if (l_val instanceof BigDecimal) {
                        operands[i] = ((BigDecimal) l_val).add(toBigDecimal(parts[0])).toString();
                    } else {
                        operands[i] = l_val.toString();
                    }
                } else {
                    ready = false;
                }
            } else if (sim.cdb.containsKey(operand)) {
                Object l_val = sim.cdb.get(operand);
                if (l_val instanceof BigInteger) {
                    operands[i] = ((BigInteger) l_val).toString(16);
                } else if (l_val instanceof BigDecimal) {
                    operands[i] = ((BigDecimal) l_val).toString();
                } else {
                    operands[i] = l_val.toString();
                }
            } else if (!operand.matches("-{0,1}[a-f0-9\\.]+")) {
                ready = false;
            }
        }
    }

    public void RATSet(Simulation sim) {
        sim.registerAddressTable.put(operands[0], opcode + "." + address);
    }

    public void RATUnset(Simulation sim) {
        if (sim.registerAddressTable.get(operands[0]) != null) {
            if (sim.registerAddressTable.get(operands[0]).equals(opcode + "." + address)) {
                sim.registerAddressTable.put(operands[0], null);

            }
        }
    }

    public void dispatch(Simulation sim) {
        if (operands.length > 0) {
            if (sim.cdb.get("rs." + opcode + "." + address) != null) {
                Register r = sim.registerFile.get(operands[0]);
                if (r != null) {
                    r.setValue(sim.cdb.get("rs." + opcode + "." + address));
                    result = r.getValue();
                    sim.cdb.remove("rs." + opcode + "." + address);
                } else {
                    System.out.println("Register not found:" + r.getName());
                }
            } else {
                System.out.println("Data not found in CDB:" + operands[0] + "\nCDB contents:" + sim.cdb);
            }
        }
    }

    String result;

    public String getResult(Simulation sim) {
        return result;
    }

    private BigInteger toBigInteger(String operand) {
        if (operand.matches("-{0,1}[a-f0-9]+")) {
            return new BigInteger(operand, 16);
        }
        System.err.println("Invalid operand conversion:" + operand);
        return new BigInteger("0");
    }

    private int getSignBit(BigDecimal num) {
        int signum = num.signum();
        return signum >= 0 ? 0 : 1;
    }

    private BigDecimal toBigDecimal(String operand) {
        if (operand.matches("-{0,1}[a-f0-9\\.]+")) {
            return new BigDecimal(new BigInteger(operand, 16).toString(10));
        }
        System.err.println("Invalid operand conversion:" + operand);
        return new BigDecimal("0");
    }

    public void execute(Simulation sim) {
        BigDecimal tfp;
        switch (opcode) {
            case "beq":
                if (toBigInteger(operands[0]).equals(toBigInteger(operands[1]))) {
                    sim.registerFile.get("pc").setValue(toBigInteger(operands[2]));
                }
                break;
            case "bne":
                if (!toBigInteger(operands[0]).equals(toBigInteger(operands[1]))) {
                    sim.registerFile.get("pc").setValue(toBigInteger(operands[2]));
                }
                break;
            case "blt":
            case "bltu":
                if (toBigInteger(operands[0]).compareTo(toBigInteger(operands[1])) < 0) {
                    sim.registerFile.get("pc").setValue(toBigInteger(operands[2]));
                }
                break;
            case "bge":
            case "bgeu":
                if (toBigInteger(operands[0]).compareTo(toBigInteger(operands[1])) >= 0) {
                    sim.registerFile.get("pc").setValue(toBigInteger(operands[2]));
                }
                break;
            case "jal":
                sim.registerFile.get((operands.length >= 2) ? operands[0] : "ra").setValue(sim.registerFile.get("pc").getValue());
                sim.registerFile.get("pc").setValue((operands.length >= 2) ? operands[1] : operands[0]);
                break;
            case "jalr":
                sim.registerFile.get((operands.length >= 2) ? operands[0] : "ra").setValue(sim.registerFile.get("pc").getValue());
                sim.registerFile.get("pc").setValue(toBigInteger((operands.length >= 2) ? operands[1] : operands[0]));
                break;
            case "ecall":
                //TODO
                break;
            case "ebreak":
                sim.pause();
                break;
            case "fence":
                //TODO
                break;
            case "fence.i":
                //TODO
                break;
            case "sll":
            case "slli":
            case "sllw":
            case "slliw":
                sim.cdb.put("rs." + opcode + "." + address, toBigInteger(operands[1]).shiftLeft(toBigInteger(operands[2]).intValue()));
                break;
            case "srl":
            case "sra":
            case "srli":
            case "srai":
            case "srlw":
            case "sraw":
            case "srliw":
            case "sraiw":
                sim.cdb.put("rs." + opcode + "." + address, toBigInteger(operands[1]).shiftRight(toBigInteger(operands[2]).intValue()));
                break;
            case "and":
            case "andi":
                sim.cdb.put("rs." + opcode + "." + address, toBigInteger(operands[1]).and(toBigInteger(operands[2])));
                break;
            case "or":
            case "ori":
                sim.cdb.put("rs." + opcode + "." + address, toBigInteger(operands[1]).or(toBigInteger(operands[2])));
                break;
            case "xor":
            case "xori":
                sim.cdb.put("rs." + opcode + "." + address, toBigInteger(operands[1]).xor(toBigInteger(operands[2])));
                break;
            case "slt":
            case "sltu":
            case "slti":
            case "sltiu":
                sim.cdb.put("rs." + opcode + "." + address, toBigInteger(operands[1]).compareTo(toBigInteger(operands[2])) < 0 ? BigInteger.ONE : BigInteger.ZERO);
                break;
            case "feq.s":
            case "feq.d":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).compareTo(toBigDecimal(operands[2])) == 0 ? BigDecimal.ONE : BigDecimal.ZERO);
                break;
            case "flt.s":
            case "flt.d":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).compareTo(toBigDecimal(operands[2])) < 0 ? BigDecimal.ONE : BigDecimal.ZERO);
                break;
            case "fle.s":
            case "fle.d":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).compareTo(toBigDecimal(operands[2])) <= 0 ? BigDecimal.ONE : BigDecimal.ZERO);
                break;
            case "fclass.s":
            case "fclass.d":
                //Dummy Mask = 0
                sim.cdb.put("rs." + opcode + "." + address, BigDecimal.ZERO);
                break;
            case "add":
            case "addi":
            case "addw":
            case "addiw":
                sim.cdb.put("rs." + opcode + "." + address, toBigInteger(operands[1]).add(toBigInteger(operands[2])));
                break;
            case "sub":
            case "subw":
                sim.cdb.put("rs." + opcode + "." + address, toBigInteger(operands[1]).subtract(toBigInteger(operands[2])));
                break;
            case "mul":
            case "mulh":
            case "mulhu":
            case "mulhsu":
            case "mulw":
                sim.cdb.put("rs." + opcode + "." + address, toBigInteger(operands[1]).multiply(toBigInteger(operands[2])));
                break;
            case "div":
            case "divu":
            case "divw":
                sim.cdb.put("rs." + opcode + "." + address, toBigInteger(operands[1]).divide(toBigInteger(operands[2])));
                break;
            case "rem":
            case "remu":
            case "remw":
            case "remuw":
                sim.cdb.put("rs." + opcode + "." + address, toBigInteger(operands[1]).remainder(toBigInteger(operands[2])));
                break;
            case "lui":
                sim.cdb.put("rs." + opcode + "." + address, toBigInteger(operands[1]).shiftLeft(12));
                break;
            case "auipc":
                sim.cdb.put("rs." + opcode + "." + address, toBigInteger(operands[1]).shiftLeft(12).add(sim.registerFile.get("pc").getIntValue()));
                break;
            case "fmv.w.x":
            case "fmv.x.w":
            case "fmv.d.x":
            case "fmv.x.d":
            case "fcvt.s.w":
            case "fcvt.d.w":
            case "fcvt.s.wu":
            case "fcvt.w.s":
            case "fcvt.w.d":
            case "fcvt.wu.s":
            case "fcvt.wu.d":
            case "fcvt.d.s":
            case "fcvt.s.d":
            case "fcvt.s.l":
            case "fcvt.d.l":
            case "fcvt.s.lu":
            case "fcvt.d.lu":
            case "fcvt.l.s":
            case "fcvt.l.d":
            case "fcvt.lu.s":
            case "fcvt.lu.d":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]));
                break;
            case "fadd.s":
            case "fadd.d":
            case "fadd.q":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).add(toBigDecimal(operands[2])));
                break;
            case "fsub.s":
            case "fsub.d":
            case "fsub.q":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).subtract(toBigDecimal(operands[2])));
                break;
            case "fmul.s":
            case "fmul.d":
            case "fmul.q":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).multiply(toBigDecimal(operands[2])));
                break;
            case "fdiv.s":
            case "fdiv.d":
            case "fdiv.q":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).divide(toBigDecimal(operands[2])));
                break;
            case "fsqrt.s":
            case "fsqrt.d":
            case "fsqrt.q":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).sqrt(MathContext.DECIMAL32));
                break;
            case "fmadd.s":
            case "fmadd.d":
            case "fmadd.q":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).multiply(toBigDecimal(operands[2])).add(toBigDecimal(operands[3])));
                break;
            case "fmsub.s":
            case "fmsub.d":
            case "fmsub.q":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).multiply(toBigDecimal(operands[2])).subtract(toBigDecimal(operands[3])));
                break;
            case "fnmsub.s":
            case "fnmsub.d":
            case "fnmsub.q":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).multiply(toBigDecimal(operands[2])).negate().add(toBigDecimal(operands[3])));
                break;
            case "fnmadd.s":
            case "fnmadd.d":
            case "fnmadd.q":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).multiply(toBigDecimal(operands[2])).negate().subtract(toBigDecimal(operands[3])));
                break;
            case "fsgnj.s":
            case "fsgnj.d":
            case "fsgnj.q":
                tfp = toBigDecimal(operands[1]);
                sim.cdb.put("rs." + opcode + "." + address, getSignBit(tfp) == getSignBit(toBigDecimal(operands[2])) ? tfp : tfp.negate());
                break;
            case "fsgnjn.s":
            case "fsgnjn.d":
            case "fsgnjn.q":
                tfp = toBigDecimal(operands[1]);
                sim.cdb.put("rs." + opcode + "." + address, getSignBit(tfp) == getSignBit(toBigDecimal(operands[2])) ? tfp.negate() : tfp);
                break;
            case "fsgnjx.s":
            case "fsgnjx.d":
            case "fsgnjx.q":
                tfp = toBigDecimal(operands[1]);
                sim.cdb.put("rs." + opcode + "." + address, (getSignBit(tfp) ^ getSignBit(toBigDecimal(operands[2]))) == getSignBit(tfp) ? tfp : tfp.negate());
                break;
            case "fmin.s":
            case "fmin.d":
            case "fmin.q":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).min(toBigDecimal(operands[2])));
                break;
            case "fmax.s":
            case "fmax.d":
            case "fmax.q":
                sim.cdb.put("rs." + opcode + "." + address, toBigDecimal(operands[1]).max(toBigDecimal(operands[2])));
                break;
            case "sb":
            case "sh":
            case "sw":
            case "sd":
            case "fsw":
            case "fsd":
            case "fsq":
                Instruction i = new Instruction();
                i.setAddress(toBigInteger(operands[1]).toString(16));
                i.setValue(toBigInteger(operands[0]).toString(16));
                sim.instructions.put(i.getAddress(), i);
                break;
            case "lb":
            case "lbu":
            case "lh":
            case "lhu":
            case "lw":
            case "ld":
            case "lwu":
            case "flw":
            case "fld":
            case "flq":
                Instruction i1 = sim.instructions.get(toBigInteger(operands[1]).toString(16));
                if (i1 != null) {
                    sim.cdb.put("rs." + opcode + "." + address, i1.hasValue() ? i1.getValue() : "0");
                } else {
                    System.out.println("Value not found at: " + operands[1]);
                    sim.cdb.put("rs." + opcode + "." + address, "0");
                }
                break;
            default:
        }
    }

    public static Instruction fromDisassemblyString(String instruction) {
        String[] i = instruction.split("#")[0].split("\t");
        Instruction ins = new Instruction();
        if (i.length >= 3) {
            i[0] = i[0].trim();
            ins.address = i[0].substring(0, i[0].length() - 1);
            if (i[2].startsWith("0x")) {
                ins.setValue(i[2].substring(2));
            } else {
                ins.opcode = i[2];
                if (i.length == 4) {
                    List<String> operands = new ArrayList<>();
                    for (String operand : i[3].split(" ")[0].split(",")) {
                        if (operand.matches("0x[0-9a-f]+")) {
                            operand = operand.substring(2);
                        }
                        if (isValidOperand(operand)) {
                            operands.add(operand);
                        } else {
                            System.err.println("Invalid operand: " + operand);
                        }
                    }
                    ins.operands = operands.toArray(new String[]{});
                } else {
                    ins.operands = new String[]{};
                }
                ins.inferType();
                if (ins.type == PSEUDO_OPS) {
                    patchPseudo(ins);
                }
            }
        }
        return ins;
    }

    /*
0 3.14
2 -1.00
4 2.72
6 0.71
8 fld ft1,0
a fld ft2,2
c fld ft3,4
e fld ft4,6
     */

    public static void patchPseudo(Instruction ins) {
        switch (ins.opcode) {
            case "nop":
            case "c.nop":
            case "unimp":
                ins.opcode = null;
                ins.operands = null;
                ins.setValue("0");
                ins.type = 0;
                break;
            case "li":
                ins.opcode = "addi";
                ins.operands = new String[]{ins.operands[0], "zero", ins.operands[1]};
                break;
            case "mv":
                ins.opcode = "addi";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], "0"};
                break;
            case "sext.w":
                ins.opcode = "addiw";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], "0"};
                break;
            case "not":
                ins.opcode = "xori";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], "-1"};
                break;
            case "neg":
                ins.opcode = "sub";
                ins.operands = new String[]{ins.operands[0], "zero", ins.operands[1]};
                break;
            case "negw":
                ins.opcode = "subw";
                ins.operands = new String[]{ins.operands[0], "zero", ins.operands[1]};
                break;
            case "seqz":
                ins.opcode = "sltiu";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], "1"};
                break;
            case "snez":
                ins.opcode = "sltu";
                ins.operands = new String[]{ins.operands[0], "zero", ins.operands[1]};
                break;
            case "sltz":
                ins.opcode = "slt";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], "1"};
                break;
            case "sgtz":
                ins.opcode = "slt";
                ins.operands = new String[]{ins.operands[0], "zero", ins.operands[1]};
                break;
            case "beqz":
                ins.opcode = "beq";
                ins.operands = new String[]{ins.operands[0], "zero", ins.operands[1]};
                break;
            case "bnez":
                ins.opcode = "bne";
                ins.operands = new String[]{ins.operands[0], "zero", ins.operands[1]};
                break;
            case "blez":
                ins.opcode = "bge";
                ins.operands = new String[]{"zero", ins.operands[0], ins.operands[1]};
                break;
            case "bgez":
                ins.opcode = "bge";
                ins.operands = new String[]{ins.operands[0], "zero", ins.operands[1]};
                break;
            case "bltz":
                ins.opcode = "blt";
                ins.operands = new String[]{ins.operands[0], "zero", ins.operands[1]};
                break;
            case "bgtz":
                ins.opcode = "blt";
                ins.operands = new String[]{"zero", ins.operands[0], ins.operands[1]};
                break;
            case "bgt":
                ins.opcode = "blt";
                ins.operands = new String[]{ins.operands[1], ins.operands[0], ins.operands[2]};
                break;
            case "ble":
                ins.opcode = "bge";
                ins.operands = new String[]{ins.operands[1], ins.operands[0], ins.operands[2]};
                break;
            case "bgtu":
                ins.opcode = "bltu";
                ins.operands = new String[]{ins.operands[1], ins.operands[0], ins.operands[2]};
                break;
            case "bleu":
                ins.opcode = "bgeu";
                ins.operands = new String[]{ins.operands[1], ins.operands[0], ins.operands[2]};
                break;
            case "fmv.s":
                ins.opcode = "fsgnj.s";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], ins.operands[1]};
                break;
            case "fabs.s":
                ins.opcode = "fsgnjx.s";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], ins.operands[1]};
                break;
            case "fneg.s":
                ins.opcode = "fsgnjn.s";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], ins.operands[1]};
                break;
            case "fmv.d":
                ins.opcode = "fsgnj.d";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], ins.operands[1]};
                break;
            case "fabs.d":
                ins.opcode = "fsgnjx.d";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], ins.operands[1]};
                break;
            case "fneg.d":
                ins.opcode = "fsgnjn.d";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], ins.operands[1]};
                break;
            case "j":
                ins.opcode = "jal";
                ins.operands = new String[]{"zero", ins.operands[0]};
                break;
            case "jr":
                ins.opcode = "jalr";
                ins.operands = new String[]{"zero", "0(" + ins.operands[0] + ")"};
                break;
            case "ret":
                ins.opcode = "jalr";
                ins.operands = new String[]{"zero", "0(ra)"};
                break;
            case "c.lw":
                ins.opcode = "lw";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], Long.toHexString(Long.parseLong(ins.operands[2]) * 4)};
                break;
            case "c.lwsp":
                ins.opcode = "lw";
                ins.operands = new String[]{ins.operands[0], "sp", Long.toHexString(Long.parseLong(ins.operands[1]) * 4)};
                break;
            case "c.flw":
                ins.opcode = "flw";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], Long.toHexString(Long.parseLong(ins.operands[2]) * 8)};
                break;
            case "c.flwsp":
                ins.opcode = "flw";
                ins.operands = new String[]{ins.operands[0], "sp", Long.toHexString(Long.parseLong(ins.operands[1]) * 8)};
                break;
            case "c.fld":
                ins.opcode = "fld";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], Long.toHexString(Long.parseLong(ins.operands[2]) * 16)};
                break;
            case "c.fldsp":
                ins.opcode = "fld";
                ins.operands = new String[]{ins.operands[0], "sp", Long.toHexString(Long.parseLong(ins.operands[1]) * 16)};
                break;
            case "c.sw":
                ins.opcode = "sw";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], Long.toHexString(Long.parseLong(ins.operands[2]) * 4)};
                break;
            case "c.swsp":
                ins.opcode = "sw";
                ins.operands = new String[]{ins.operands[0], "sp", Long.toHexString(Long.parseLong(ins.operands[1]) * 4)};
                break;
            case "c.fsw":
                ins.opcode = "fsw";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], Long.toHexString(Long.parseLong(ins.operands[2]) * 8)};
                break;
            case "c.fswsp":
                ins.opcode = "fsw";
                ins.operands = new String[]{ins.operands[0], "sp", Long.toHexString(Long.parseLong(ins.operands[1]) * 8)};
                break;
            case "c.fsd":
                ins.opcode = "fsd";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], Long.toHexString(Long.parseLong(ins.operands[2]) * 16)};
                break;
            case "c.fsdsp":
                ins.opcode = "fsd";
                ins.operands = new String[]{ins.operands[0], "sp", Long.toHexString(Long.parseLong(ins.operands[1]) * 16)};
                break;

            case "c.add":
                ins.opcode = "add";
                ins.operands = new String[]{ins.operands[0], ins.operands[0], ins.operands[1]};
                break;
            case "c.addi":
                ins.opcode = "addi";
                ins.operands = new String[]{ins.operands[0], ins.operands[0], ins.operands[1]};
                break;
            case "c.addi16sp":
                ins.opcode = "addi";
                ins.operands = new String[]{"sp", "sp", Long.toHexString(Long.parseLong(ins.operands[1]) * 16)};
                break;
            case "c.addi4spn":
                ins.opcode = "addi";
                ins.operands = new String[]{ins.operands[0], "sp", Long.toHexString(Long.parseLong(ins.operands[1]) * 16)};
                break;
            case "c.sub":
                ins.opcode = "sub";
                ins.operands = new String[]{ins.operands[0], ins.operands[0], ins.operands[1]};
                break;
            case "c.and":
                ins.opcode = "and";
                ins.operands = new String[]{ins.operands[0], ins.operands[0], ins.operands[1]};
                break;
            case "c.andi":
                ins.opcode = "andi";
                ins.operands = new String[]{ins.operands[0], ins.operands[0], ins.operands[1]};
                break;
            case "c.or":
                ins.opcode = "or";
                ins.operands = new String[]{ins.operands[0], ins.operands[0], ins.operands[1]};
                break;
            case "c.xor":
                ins.opcode = "xor";
                ins.operands = new String[]{ins.operands[0], ins.operands[0], ins.operands[1]};
                break;
            case "c.mv":
                ins.opcode = "add";
                ins.operands = new String[]{ins.operands[0], ins.operands[1], "zero"};
                break;
            case "c.li":
                ins.opcode = "addi";
                ins.operands = new String[]{ins.operands[0], "zero", ins.operands[1]};
                break;
            case "c.lui":
                ins.opcode = "lui";
                break;
            case "c.slli":
                ins.opcode = "slli";
                ins.operands = new String[]{ins.operands[0], ins.operands[0], ins.operands[1]};
                break;
            case "c.srai":
                ins.opcode = "srai";
                ins.operands = new String[]{ins.operands[0], ins.operands[0], ins.operands[1]};
                break;
            case "c.srli":
                ins.opcode = "srli";
                ins.operands = new String[]{ins.operands[0], ins.operands[0], ins.operands[1]};
                break;
            case "c.beqz":
                ins.opcode = "beq";
                ins.operands = new String[]{ins.operands[0], "zero", ins.operands[1]};
                break;
            case "c.bnez":
                ins.opcode = "bne";
                ins.operands = new String[]{ins.operands[0], "zero", ins.operands[1]};
                break;
            case "c.j":
                ins.opcode = "jal";
                ins.operands = new String[]{"zero", ins.operands[0]};
                break;
            case "c.jr":
                ins.opcode = "jalr";
                ins.operands = new String[]{"zero", ins.operands[1], "zero"};
                break;
            case "c.jal":
                ins.opcode = "jal";
                ins.operands = new String[]{"ra", ins.operands[0]};
                break;
            case "c.jalr":
                ins.opcode = "jalr";
                ins.operands = new String[]{"ra", ins.operands[0], "zero"};
                break;
            case "c.ebreak":
                ins.opcode = "ebreak";
                break;
            default:
                System.err.println("Error decoding pseudo instruction: " + ins);
                break;
        }
        if (ins.opcode != null) {
            ins.inferType();
        }
    }

    /**
     * @return the opcode
     */
    public String getOpcode() {
        return opcode;
    }

    /**
     * @param opcode the opcode to set
     */
    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    /**
     * @return the operand
     */
    public String[] getOperands() {
        return operands;
    }

    /**
     * @param operands the operand to set
     */
    public void setOperands(String operands) {
        this.operands = operands.split(",");
    }

    @Override
    public String toString() {
        if (hasValue()) {
            return address + " " + getValue();
        } else if (opcode != null) {
            return address + " " + opcode + " " + String.join(",", operands);
        } else {
            return "unrecognized instruction";
        }
    }

    public boolean isValid() {
        return hasValue() || opcode != null;
    }

    public boolean hasValue() {
        return getValue() != null;
    }
    private int type;

    private void inferType() {
        List<String> control_ops_list = Arrays.asList(control_transfer_ops),
                bitwise_ops_list = Arrays.asList(bitwise_ops),
                arithmetic_ops_list = Arrays.asList(arithmetic_ops),
                load_ops_list = Arrays.asList(load_ops),
                store_ops_list = Arrays.asList(store_ops),
                pseudo_ops_list = Arrays.asList(pseudo_ops);
        type = control_ops_list.contains(this.opcode) ? CONTROL_TRANSFER_OPS
                : bitwise_ops_list.contains(this.opcode) ? BITWISE_OPS
                : arithmetic_ops_list.contains(this.opcode) ? ARITHMETIC_OPS
                : store_ops_list.contains(this.opcode) ? STORE_OPS
                : load_ops_list.contains(this.opcode) ? LOAD_OPS
                : pseudo_ops_list.contains(this.opcode) ? PSEUDO_OPS
                : UNKNOWN_OPS;
    }

    public int getType() {
        return type;
    }

    public String getTypeString() {
        int type = getType();
        return type == CONTROL_TRANSFER_OPS ? "Control Transfer"
                : type == BITWISE_OPS ? "Bitwise"
                        : type == ARITHMETIC_OPS ? "Arithmetic"
                                : type == LOAD_OPS ? "Load"
                                        : type == STORE_OPS ? "Store"
                                                : type == PSEUDO_OPS ? "Pseudo"
                                                        : "Unknown";
    }
    private String opcode, address, value, operands[];
}
