package Graph;
import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.VBuiltIn.Op;

import cs132.util.*;
import cs132.vapor.ast.*;
import cs132.vapor.parser.*;

import java.io.*;
import java.util.*;

import Graph.Pair;




public class LinearScan {
    Vector<Pair> active; //should be sorted by increasing end point
    public Vector<String> register_list;
    public Vector<String> local_list;

    Map<Integer, Vector<String>> registers;
    Map<Integer, Vector<String>> locals;


    public LinearScan() {
        active = null;
        register_list = new Vector<>();
        local_list = new Vector<>();
        registers = new HashMap<>();
        locals = new HashMap<>();
    }

    public void print_reg() {
        System.out.println("    register mappings:");
        for(int regnum = 0; regnum < register_list.size(); regnum++) {
            System.out.println("        t" + regnum + " holds " + register_list.get(regnum));
        }
    }

    public void print_local() {
        System.out.println("    local mappings:");
        for(int locnum = 0; locnum < local_list.size(); locnum++) {
            System.out.println("        Local_list[" + locnum + "] holds " + local_list.get(locnum)); 
        }
    }

    public void print_reg_map() {
        for(int i = 0; i < 1000; i++) {

            if(registers.containsKey(i)) {
                Vector<String> regs = registers.get(i);
                System.out.println("    In line " + i);
                for(int regnum = 0; regnum < regs.size(); regnum++) {
                    System.out.println("            t" + regnum + " holds " + regs.get(regnum));
                } 
            }
        }
    }

    public void print_local_map() {
        for(int i: locals.keySet()) {
            Vector<String> locs = locals.get(i);
            for(int locnum = 0; locnum < locs.size(); locnum++) {
                System.out.println("            Local_list[" + locnum + "] holds " + locs.get(locnum));
            }
        }
    }

    public void LinearScanRegisterAllocation(Vector<Pair> all_intervals, VisitorGraphBuilder v) {
        for(int i = 0; i < 9; i++) {
            register_list.add("");
        }
        active = new Vector<>();

        for(int i = 0; i < all_intervals.size(); i++) {
            System.out.println(all_intervals.get(i).key);

            // if(i == 0) {
            //     Vector<String> first_vec = new Vector<String>(register_list);
            //     registers.put(all_intervals.get(i).value.start.line, first_vec);
            // }
            
            ExpireOldIntervals(all_intervals.get(i));

            if(active.size() == register_list.size()) {
                System.out.println("    1: " + active.size() + " " + register_list.size());
                SpillAtInterval(all_intervals.get(i));

            }
            else {
                System.out.println("    2: " + all_intervals.size() + " active size is " + active.size() + " and register size is " + register_list.size());
                int free_reg = -1;

                for(int j = 0; j < register_list.size(); j++) {
                    if(register_list.get(j) == "") {
                        free_reg = j;
                    }
                }
                register_list.setElementAt(all_intervals.get(i).key, free_reg);


                active.add(all_intervals.get(i));

                for(int k = 0; k < active.size() - 1; k++) {
                    int min = k;
                    for(int l = k + 1; l < active.size(); l++) {
                        if(active.get(l).value.end.line < active.get(min).value.end.line) {
                            min = l;
                        }
                    }
                    Pair tmp3 = active.get(min);
                    active.setElementAt(active.get(k), min);
                    active.setElementAt(tmp3, k);
                }

            }

            System.out.println("### Registers at interval " + all_intervals.get(i).key + " at line " + all_intervals.get(i).value.start.line);
            print_reg();
            print_local();
            Vector<String> new_reg_list = new Vector<String>(register_list);
            Vector<String> new_local_list = new Vector<String>(local_list);

            if(registers.containsKey(all_intervals.get(i).value.start.line)) {
                registers.replace(all_intervals.get(i).value.start.line, new_reg_list);
                // System.out.println("1");
            }
            else {
                registers.put(all_intervals.get(i).value.start.line, new_reg_list);
                // System.out.println("2");
            }


            if(locals.containsKey(all_intervals.get(i).value.start.line)) {
                locals.replace(all_intervals.get(i).value.start.line, new_local_list);
            }
            else {
                locals.put(all_intervals.get(i).value.start.line, new_local_list);
            }

        }

    }

    public void ExpireOldIntervals(Pair i) {
        System.out.println("    Expiring for " + i.key);
        for(int j = 0; j < active.size(); j++) {

            if(active.get(j).value.end.line >= i.value.start.line) {
            }
            else {
                System.out.println("        Expiring " + active.get(j).key + " which ends before " + i.key);
                for(int k = 0; k < register_list.size(); k++) {
                    if(register_list.get(k) == active.get(j).key) {
                        register_list.setElementAt("", k);
                    }
                }
                active.remove(j);
            }
        }
    }

    public void SpillAtInterval(Pair i) {
        System.out.println("attempting to spill " + i.key);
        Pair spill = active.get(active.size() - 1);
        if(spill.value.end.line > i.value.end.line) {

            int free_reg = -1;
            for(int j = 0; j < register_list.size(); j++) {
                if(register_list.get(j) == spill.key) {
                    free_reg = j;
                }
            }
            System.out.println("Spilling " + spill.key + " at register " + free_reg + " to store " + i.key);
            register_list.setElementAt(i.key, free_reg);
            local_list.add(spill.key);

            active.remove(active.size() - 1);
            active.add(i);

            for(int k = 0; k < active.size() - 1; k++) {
                int min = k;
                for(int l = k + 1; l < active.size(); l++) {
                    if(active.get(l).value.end.line < active.get(min).value.end.line) {
                        min = l;
                    }
                }
                Pair tmp3 = active.get(min);
                active.setElementAt(active.get(k), min);
                active.setElementAt(tmp3, k);
            }
            
        }
        else {
            System.out.println("Spilling " + i.key + " to keep " + spill.key);
            local_list.add(i.key);
        }

    }


}