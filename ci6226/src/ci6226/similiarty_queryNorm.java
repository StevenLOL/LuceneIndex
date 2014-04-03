/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ci6226;

import org.apache.lucene.search.similarities.DefaultSimilarity;

/**
 *
 * @author xiaohai
 */
public class similiarty_queryNorm extends DefaultSimilarity {   
    @Override
    public float queryNorm(float sumOfSquaredWeights) {
        return 1/sumOfSquaredWeights;
    }
}
