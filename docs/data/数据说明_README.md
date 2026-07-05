# Gold 红酒销售管理系统种子数据说明

## 文件内容

- `origin_seed.csv`：产地表，共 18 条，符合项目中 origin 表设计。
- `wine_seed_118.csv`：红酒表，共 118 条，包含品牌、类型、年份、产地、进货价、销售价、库存、状态、来源链接。
- `gold_wine_seed_sqlite.sql`：SQLite 可执行建表和插入脚本，可直接给 Java + JDBC 项目初始化使用。
- `wine_seed_workbook.xlsx`：Excel 版本，包含 origin、wine、source_manifest 三个工作表。

## 使用建议

你的项目计划中原本的 wine 表字段是：
id, name, brand, type, year, origin_id, purchase_price, sale_price, stock, status。

我额外加了两个字段：
- grape_varieties：葡萄品种，方便查询和答辩说明。
- source_url：数据来源链接，方便说明“数据不是乱编的”。

如果 Codex 写项目时只想严格按原字段建表，把 CSV / SQL 里的 `grape_varieties` 和 `source_url` 两列删掉即可。

## 价格说明

价格是课程项目演示用的合理人民币零售价区间口径，不是实时电商报价。
进货价按销售价约 68% 估算，方便后续做利润或销售统计演示。

## 数据来源依据

数据参考公开品牌/酒庄官网、常见葡萄酒品牌信息与产区信息整理，包括：
Penfolds, Mouton Cadet, Lafite Rothschild, Louis Jadot, Moët & Chandon, Antinori, Banfi, Marqués de Riscal, Robert Mondavi, Casillero del Diablo, Concha y Toro, Catena Zapata, Graham's, Sandeman, Cloudy Bay 等官网或品牌页面。
每条 wine 记录保留了 source_url。
