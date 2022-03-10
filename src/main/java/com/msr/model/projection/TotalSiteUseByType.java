package com.msr.model.projection;

/**
 * Projection for displaying Primary use types by Site
 */
public interface TotalSiteUseByType {

	Integer getUseTypeId();
	String getUseTypeName();
	Long getTotalSize();
	Integer getSiteId();
}