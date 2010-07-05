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

import org.pentaho.pat.rpc.dto.enums.SelectionType;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * DTO Version of org.olap4j.query.Selection
 * @created Jul 5, 2010 
 * @since 0.8
 * @author Paul Stoellberger
 * 
 */
public class PatQuerySelection implements Serializable, IsSerializable {


    private static final long serialVersionUID = -5715168393520227994L;
    private String uniqueName;
    private String caption;
    private SelectionType operator = SelectionType.MEMBER;

    /**
     * Default Zero-Argument Constructor for Serialization
     * DO NOT USE!!!
     */
    public PatQuerySelection() {};
    
    public PatQuerySelection(String uniqueName, String caption, SelectionType operator) {
        this.uniqueName = uniqueName;
        this.caption = caption;
        this.operator  = operator;
    }

    public String getCaption() {
        return caption;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public SelectionType getOperator() {
        return operator;
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((operator == null) ? 0 : operator.toString().hashCode());
        result = prime * result + ((uniqueName == null) ? 0 : uniqueName.hashCode());
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
        PatQuerySelection other = (PatQuerySelection) obj;
        if (operator == null) {
            if (other.operator != null)
                return false;
        } else if (!operator.equals(other.operator))
            return false;
        if (uniqueName == null) {
            if (other.uniqueName != null)
                return false;
        } else if (!uniqueName.equals(other.uniqueName)) {
            return false;
        }
        else if (operator.equals(other.operator)) {
            return true;
        }

        return false;
    }
    
    

}
