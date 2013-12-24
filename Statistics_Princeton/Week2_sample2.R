# Statistics One, Lecture 6, example scripts
# Test/re-test reliability analysis, column format
library(psych)

impact.row <- read.table("supplemental_STATS1.EX.03.ROW.txt", header=T)

describe.by(impact.row, impact.row$test)

cor(impact.row$memory.verbal[impact.row$test=='A'], impact.row$memory.verbal[impact.row$test=='B'])