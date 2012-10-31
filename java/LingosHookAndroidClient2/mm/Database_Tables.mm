<map version="1.0.0">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="1283093380553" ID="ID_1723255651" MODIFIED="1351066129612" TEXT="Database Tables">
<hook NAME="MapStyle">
<properties SHOW_ICON_FOR_ATTRIBUTES="true" SHOW_NOTES_IN_MAP="false" SHOW_NOTE_ICONS="true"/>
<map_styles>
<stylenode LOCALIZED_TEXT="styles.root_node">
<stylenode LOCALIZED_TEXT="styles.predefined" POSITION="right">
<stylenode COLOR="#000000" LOCALIZED_TEXT="default" MAX_WIDTH="600" STYLE="as_parent">
<font/>
</stylenode>
<stylenode LOCALIZED_TEXT="defaultstyle.details"/>
<stylenode LOCALIZED_TEXT="defaultstyle.note"/>
<stylenode LOCALIZED_TEXT="defaultstyle.floating">
<edge/>
<cloud/>
</stylenode>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.user-defined" POSITION="right">
<stylenode COLOR="#18898b" LOCALIZED_TEXT="styles.topic" STYLE="fork">
<font/>
</stylenode>
<stylenode COLOR="#cc3300" LOCALIZED_TEXT="styles.subtopic" STYLE="fork">
<font/>
</stylenode>
<stylenode COLOR="#669900" LOCALIZED_TEXT="styles.subsubtopic">
<font/>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.important">
<icon/>
</stylenode>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.AutomaticLayout" POSITION="right">
<stylenode COLOR="#000000" LOCALIZED_TEXT="AutomaticLayout.level.root">
<font/>
</stylenode>
<stylenode COLOR="#0033ff" LOCALIZED_TEXT="AutomaticLayout.level,1">
<font/>
</stylenode>
<stylenode COLOR="#00b439" LOCALIZED_TEXT="AutomaticLayout.level,2">
<font/>
</stylenode>
<stylenode COLOR="#990000" LOCALIZED_TEXT="AutomaticLayout.level,3">
<font/>
</stylenode>
<stylenode COLOR="#111111" LOCALIZED_TEXT="AutomaticLayout.level,4">
<font/>
</stylenode>
</stylenode>
</stylenode>
</map_styles>
</hook>
<hook NAME="AutomaticEdgeColor"/>
<node CREATED="1350984011029" ID="ID_1991406085" MODIFIED="1351064657351" POSITION="right" TEXT="WordTable" VSHIFT="-50">
<edge COLOR="#ff0000" WIDTH="3"/>
<node CREATED="1350984034643" ID="ID_465252376" MODIFIED="1350984040662" TEXT="wordid"/>
<node CREATED="1350984019639" ID="ID_276174464" MODIFIED="1350984028664" TEXT="word"/>
<node CREATED="1351065569579" ID="ID_969018033" MODIFIED="1351065574725" TEXT="flag">
<node CREATED="1351065840516" ID="ID_740083382" MODIFIED="1351065850314" TEXT="1:dict"/>
<node CREATED="1351065851509" ID="ID_697943359" MODIFIED="1351065859042" TEXT="2: memory"/>
<node CREATED="1351065860249" ID="ID_1501010520" MODIFIED="1351065872217" TEXT="4: dict &amp; memory"/>
</node>
</node>
<node CREATED="1351064660831" ID="ID_607019814" MODIFIED="1351245213881" POSITION="right" TEXT="SrcTable" VSHIFT="-40">
<edge COLOR="#00ff00" WIDTH="3"/>
<node CREATED="1351064679521" ID="ID_1118126146" MODIFIED="1351065600476" TEXT="srcid"/>
<node CREATED="1351065612593" ID="ID_504427405" MODIFIED="1351065616214" TEXT="wordid"/>
<node CREATED="1351065642351" ID="ID_439567517" MODIFIED="1351065818853" TEXT="fmt">
<node CREATED="1351065887693" ID="ID_1211440503" MODIFIED="1351065894450" TEXT="1: text"/>
<node CREATED="1351065895327" ID="ID_805290053" MODIFIED="1351065899360" TEXT="2: html"/>
<node CREATED="1351065904427" ID="ID_1781477498" MODIFIED="1351580334320" TEXT="3: xml">
<attribute NAME="template" VALUE="&lt;x&gt;&lt;W&gt;Word&lt;/W&gt; &lt;D&gt;dictid&lt;/D&gt; &lt;E&gt;E1&lt;/E&gt; &lt;E&gt;E2&lt;/E&gt; &lt;F&gt; &lt;S&gt;Symbol&lt;/S&gt; &lt;L&gt;Link&lt;/L&gt; &lt;I&gt; &lt;C&gt;category&lt;/C&gt; &lt;M&gt;Meaning&lt;/M&gt; &lt;/I&gt; &lt;I&gt; &lt;C&gt;category&lt;/C&gt; &lt;M&gt;Meaning&lt;/M&gt; &lt;/I&gt; &lt;/F&gt; &lt;F&gt; &lt;S&gt;Symbol&lt;/S&gt; &lt;L&gt;Link&lt;/L&gt; &lt;I&gt; &lt;C&gt;category&lt;/C&gt; &lt;M&gt;Meaning&lt;/M&gt; &lt;/I&gt; &lt;/F&gt;&lt;/X&gt;"/>
</node>
</node>
<node CREATED="1351065821519" ID="ID_452731331" MODIFIED="1351065837984" TEXT="orig">
<node CREATED="1351065936346" ID="ID_313569321" MODIFIED="1351065942633" TEXT="1:vicon"/>
<node CREATED="1351065943800" ID="ID_1626732890" MODIFIED="1351065950558" TEXT="2: lingoshook"/>
<node CREATED="1351065951610" ID="ID_1475800425" MODIFIED="1351065971681" TEXT="3: input from xml"/>
<node CREATED="1351065994214" ID="ID_1636905199" MODIFIED="1351066005565" TEXT="4: input from client"/>
</node>
<node CREATED="1351066614578" ID="ID_457246419" MODIFIED="1351066619639" TEXT="content"/>
</node>
<node CREATED="1346401472760" ID="ID_652228933" MODIFIED="1351066873204" POSITION="right" TEXT="score">
<edge COLOR="#ff00ff" WIDTH="3"/>
<font NAME="SansSerif" SIZE="12"/>
<hook NAME="FirstGroupNode"/>
<node CREATED="1346401489484" ID="ID_718003594" MODIFIED="1350012037610" TEXT="wordid">
<font NAME="SansSerif" SIZE="12"/>
</node>
<node CREATED="1346401655347" ID="ID_1646778938" MODIFIED="1350012037610" TEXT="last">
<font NAME="SansSerif" SIZE="12"/>
</node>
<node CREATED="1346401660185" ID="ID_1030407128" MODIFIED="1350012037610" TEXT="next">
<font NAME="SansSerif" SIZE="12"/>
</node>
<node CREATED="1346401664369" ID="ID_109601736" MODIFIED="1350012037609" TEXT="score">
<font NAME="SansSerif" SIZE="12"/>
</node>
</node>
<node CREATED="1346400607120" ID="ID_1954381151" MODIFIED="1351066889773" POSITION="right" TEXT="dictionay">
<edge COLOR="#00ffff" WIDTH="3"/>
<font NAME="SansSerif" SIZE="12"/>
<node CREATED="1346400668581" ID="ID_71748725" MODIFIED="1350012037611" TEXT="dictid">
<font NAME="SansSerif" SIZE="12"/>
</node>
<node CREATED="1346400674613" ID="ID_809035949" MODIFIED="1350012037611" TEXT="title">
<font NAME="SansSerif" SIZE="12"/>
</node>
</node>
</node>
</map>
