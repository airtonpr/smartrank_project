package method.advisor;

import java.util.HashSet;
import java.util.Set;

/*
 * Tem um destes para cada m�todo (@MethodNode), e ter� um ou mais @NodeDependence
 */
public class ClusterDependence {
	private Set<Dependence> dependences;

	public ClusterDependence(){
		dependences = new HashSet<Dependence>();
	}

	public Set<Dependence> getDependences() {
		return dependences;
	}


	public void setDependences(Set<Dependence> dependences) {
		this.dependences = dependences;
	}

	public boolean hasDependence(Dependence dep){
		for (Dependence dep2 : dependences) {
			if (dep2.hasDependence(dep)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (Dependence dep : dependences) {
			s += dep +"\n";
		}
		return s;
	}

}