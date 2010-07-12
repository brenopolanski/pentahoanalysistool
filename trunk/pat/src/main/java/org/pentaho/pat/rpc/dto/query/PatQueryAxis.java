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
 * PAT DTO Version of see org.olap4j.query.QueryAxis
 * @created Jul 5, 2010 
 * @since 0.8
 * @author Paul Stoellberger
 * 
 */
public class PatQueryAxis implements Serializable, IsSerializable {

   
    private static final long serialVersionUID = -8542035138458270559L;
    private IAxis.Standard location;
    private List<PatQueryDimension> dimensionList = new ArrayList<PatQueryDimension>();;

    /**
     * Default Zero-Argument Constructor for Serialization
     * DO NOT USE!!!
     */
    public PatQueryAxis() {};
    
    public PatQueryAxis(IAxis.Standard location) {
        this.location = location;
    }
    
    public void addDimension(PatQueryDimension dimension) {
        dimensionList.add(dimension);
    }
    
    public void removeDimension(PatQueryDimension dimension) {
        dimensionList.remove(dimension);
    }
    
    public List<PatQueryDimension> getDimensions() {
        return dimensionList;
    }
    
    public IAxis getLocation() {
        return location;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((location == null) ? 0 : location.hashCode());
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
        PatQueryAxis other = (PatQueryAxis) obj;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!(location.axisOrdinal() == other.location.axisOrdinal()))
            return false;
        return true;
    }


}
