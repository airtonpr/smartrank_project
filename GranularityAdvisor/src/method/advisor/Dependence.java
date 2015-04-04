package method.advisor;


public class Dependence {
	private NodeDependence assign;
	private NodeDependence call;

	public Dependence(NodeDependence assign, NodeDependence call) {
		this.assign = assign;
		this.call = call;
	}

	@Override
	public String toString() {
		if (assign == null) {
			return call+"";
		}else{
			return assign + " > " + call ;
		}
	}
	
	public NodeDependence getAssign() {
		return assign;
	}

	public void setAssign(NodeDependence assign) {
		this.assign = assign;
	}

	public NodeDependence getCall() {
		return call;
	}

	public void setCall(NodeDependence call) {
		this.call = call;
	}

	public boolean hasDependence(Dependence o2) {
		if (this.getAssign() != null && this.getAssign().equals(o2.getCall())){
			return true;
		}else if (o2.getAssign() != null && o2.getAssign().equals(this.getCall())){
			return true;
		}else if (this.getCall().equals(o2.getCall())){
			return true;
		}else if (o2.getAssign() != null && this.getAssign() != null && o2.getAssign().equals(this.getAssign())){
			return true;
		}
		return false;
	}

	

}