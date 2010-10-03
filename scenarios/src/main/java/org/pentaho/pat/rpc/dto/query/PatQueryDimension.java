/*
 * Copyright (C) 2009 Paul Stoellberger
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.rpc.dto.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * PAT DTO Version of org.olap4j.query.QueryDimension
 * @created Jul 5, 2010 
 * @since 0.8
 * @author Paul Stoellberger
 * 
 */
public class PatQueryDimension implements Serializable, IsSerializable {
    

    private static final long serialVersionUID = -7711099775825047913L;
    private String name;
    private List<PatQuerySelection> inclusions = new ArrayList<PatQuerySelection>();
    private List<PatQuerySelection> exclusions = new ArrayList<PatQuerySelection>();
    
    /**
     * Default Zero-Argument Constructor for Serialization
     * DO NOT USE!!!
     */
    public PatQueryDimension() {};
    
    public PatQueryDimension(String name) {
        this.name = name;
    }
    
    public void addInclusion(PatQuerySelection selection) {
        inclusions.add(selection);
    }
    
    public void removeInclusion(PatQuerySelection selection) {
        inclusions.remove(selection);
    }

    
    public void addExclusion(PatQuerySelection selection) {
        exclusions.add(selection);
    }
    
    public void removeExclusion(PatQuerySelection selection) {
        exclusions.remove(selection);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PatQueryDimension other = (PatQueryDimension) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
