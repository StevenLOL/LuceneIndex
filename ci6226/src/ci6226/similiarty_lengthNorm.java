/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ci6226;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.DefaultSimilarity;

/**
 *
 * @author xiaohai
 */
public class similiarty_lengthNorm extends DefaultSimilarity {   
    @Override
    public float lengthNorm(FieldInvertState state) {
        return 1/state.getUniqueTermCount();
    }
}
