/* Soot - a J*va Optimization Framework
 * Copyright (C) 2002 Ondrej Lhotak
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package soot.jimple;
import java.util.*;
import soot.jimple.spark.*;
import soot.*;
import java.io.*;

/** Dumps the reaching types of each local variable to a file in a format that
 * can be easily compared with results of other analyses, such as VTA.
 * @author Ondrej Lhotak
 */
public class ReachingTypeDumper {
    public ReachingTypeDumper( PointsToAnalysis pa ) {
        this.pa = pa;
    }
    public void dump() {
        try {
            PrintWriter file = new PrintWriter(
                new FileOutputStream( "types" ) );
            for( Iterator it = Scene.v().getApplicationClasses().iterator();
                    it.hasNext(); ) {
                handleClass( file, (SootClass) it.next() );
            }
            for( Iterator it = Scene.v().getLibraryClasses().iterator();
                    it.hasNext(); ) {
                handleClass( file, (SootClass) it.next() );
            }
        } catch( IOException e ) {
            throw new RuntimeException( "Couldn't dump reaching types."+e );
        }
    }


    /* End of public methods. Nothing to see here; move along. */
    /* End of package methods. Nothing to see here; move along. */

    protected PointsToAnalysis pa;

    protected void handleClass( PrintWriter out, SootClass c ) {
        for( Iterator it = c.getMethods().iterator(); it.hasNext(); ) {
            SootMethod m = (SootMethod) it.next();
            Body b = m.getActiveBody();
            TreeSet locals = new TreeSet( b.getLocals() );
            for( Iterator lIt = locals.iterator(); lIt.hasNext(); ) {
                Local l = (Local) lIt.next();
                out.println( "V "+l );
                if( l.getType() instanceof RefLikeType ) {
                    Set types = pa.reachingObjects( m, null, l ).possibleTypes();
                    TreeSet sortedTypes = new TreeSet( types );
                    for( Iterator tIt = sortedTypes.iterator(); tIt.hasNext(); ) {
                        out.println( "T "+tIt.next() );
                    }
                }
            }
        }
    }
}

