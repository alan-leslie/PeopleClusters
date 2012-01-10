<?xml version="1.0" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml" indent="yes"/>
	<xsl:template match="cluster">
	<clade><xsl:apply-templates/></clade>
	</xsl:template>
	
	<xsl:template match="name">
	<xsl:copy>
	<xsl:apply-templates/>
        </xsl:copy>
	</xsl:template>
	
	<xsl:template match="/">
<phyloxml xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.phyloxml.org http://www.phyloxml.org/1.10/phyloxml.xsd" xmlns="http://www.phyloxml.org">
<phylogeny rooted="true">
		<xsl:apply-templates select="cluster"/>
</phylogeny>
</phyloxml>
	</xsl:template>
	
</xsl:stylesheet>
