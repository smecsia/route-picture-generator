package com.triptomap.libs.picture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.addAll;
import static org.apache.commons.lang3.StringUtils.join;

/**
 * @author smecsia
 */
public class TestAngles {

    public static void main(String[] args) {
        List<Double> angles = new ArrayList<Double>();
        addAll(angles, new Double[]{110.31, 45.54});
        TestAngles test = new TestAngles();
        Set<List<Double>> variants = new HashSet<List<Double>>();
        test.calcVariants(variants, angles, 0);
        for(List<Double> variant : variants){
            test.doPrint(variant);
        }
    }

    public void calcVariants(Set<List<Double>> variants, List<Double> angles, int currentIdx) {
        if (currentIdx > angles.size()) {
            return;
        }
        variants.add(angles);
        for (int i = currentIdx; i < angles.size(); i++) {
            List<Double> newAngles = new ArrayList<Double>();
            addAll(newAngles, angles);
            newAngles.set(i, angles.get(i) - 1);
            calcVariants(variants, newAngles, i + 1); // calc with -deviation

            newAngles = new ArrayList<Double>();
            addAll(newAngles, angles);
            newAngles.set(i, angles.get(i) + 1);
            calcVariants(variants, newAngles, i + 1); // calc with +deviation
        }
    }

    public void doPrint(List<Double> angles) {
        List<Integer> intAngles = new ArrayList<Integer>();
        for (int i = 0; i < angles.size(); ++i) {
            intAngles.add(angles.get(i).intValue());
        }
        System.out.println(join(intAngles, ","));
    }
}
