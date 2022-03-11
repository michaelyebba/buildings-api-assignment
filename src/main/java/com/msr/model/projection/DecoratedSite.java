package com.msr.model.projection;

/**
 * Projection for v_DecoratedSite
 */
public interface DecoratedSite {

	Integer getId();
	String getAddress();
	String getCity();
	String getName();
	String getState();
	String getZipCode();
	Long getTotalSizeSqft();
	Integer getUseTypeId();
	String getUseTypeName();
}
