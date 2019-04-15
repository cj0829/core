package org.csr.core.persistence.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchResult{
	private List<Long> addIds;
	private List<Long> deleteIds;
	
	/**
	 * matchFunctionPoint:  匹配功能id是否 <br/>
	 * @author caijin
	 * @param functionPoints 功能点
	 * @param ids 需要匹配的功能点
	 * @return
	 * @since JDK 1.7
	 */
	public static MatchResult matchFunctionPoint(List<Long> functionPoints,Long[] ids){
		MatchResult match=new MatchResult();
		if(functionPoints==null){
			match.addIds=Arrays.asList(ids);
		}else{
			match.addIds=new ArrayList<Long>();
			match.deleteIds=new ArrayList<Long>();
			for (int n = 0; n < ids.length; n++) {
				boolean bool=true;
				for (int i = 0; i < functionPoints.size(); i++) {
					if (functionPoints.get(i).equals(ids[n])) {
						functionPoints.remove(i);
						bool=false;
						break;
					}
				}
				if(bool){
					match.addIds.add(ids[n]);
				}
			}
			for (int i=0;functionPoints!=null && i<functionPoints.size();i++){
				match.deleteIds.add(functionPoints.get(i));
			}
		}
		return match;
	}

	public List<Long> getAddIds() {
		return addIds;
	}

	public List<Long> getDeleteIds() {
		return deleteIds;
	}

	public void setAddIds(List<Long> addIds) {
		this.addIds = addIds;
	}

	public void setDeleteIds(List<Long> deleteIds) {
		this.deleteIds = deleteIds;
	}
}