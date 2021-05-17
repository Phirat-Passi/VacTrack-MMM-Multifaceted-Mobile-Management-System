library(data.table)
library(lattice)
# Needs unzipped VAERS library from:
# https://vaers.hhs.gov/data/datasets.html
# Below is full 2020 data and (paritial) April 2021 data

# File folder should contain like this:
# 2020VAERSDATA.csv
# 2020VAERSSYMPTOMS.csv
# 2020VAERSVAX.csv 

# merge routines:
# All 2020 data
setwd("D:\\Politics\\VAERS\\2020VAERSData.All.2020")
Data_Vax <- merge(fread("2020VAERSDATA.csv"),fread("2020VAERSVAX.csv"),all.x=TRUE,by="VAERS_ID")
setkey(Data_Vax,VAERS_ID)
Data_Vax_SYMP <- merge(Data_Vax,fread("2020VAERSSYMPTOMS.csv"),all.x=TRUE,by="VAERS_ID")
setkey(Data_Vax_SYMP,VAERS_ID)
print("All merged db entry count");nrow(Data_Vax_SYMP)
print("Duplicated VAERS_ID count");Data_Vax_SYMP[duplicated(VAERS_ID),.N]
print("Not duplicated VAERS_ID count");Data_Vax_SYMP[!duplicated(VAERS_ID),.N]
# remove duplicated VAERS_ID...still a little hazy on the what/why of dual VAERS_ID entries:
Data_Vax_SYMP_2020 <- Data_Vax_SYMP
mergeDVS <- Data_Vax_SYMP[!duplicated(VAERS_ID),]
mergeDVS2020 <- mergeDVS

# through April 02 2021 data
setwd("D:\\Politics\\VAERS\\2021VAERSData.04.02.2021")
Data_Vax <- merge(fread("2021VAERSDATA.csv"),fread("2021VAERSVAX.csv"),all.x=TRUE,by="VAERS_ID")
setkey(Data_Vax,VAERS_ID)
Data_Vax_SYMP <- merge(Data_Vax,fread("2021VAERSSYMPTOMS.csv"),all.x=TRUE,by="VAERS_ID")
setkey(Data_Vax_SYMP,VAERS_ID)
print("All merged db entry count");nrow(Data_Vax_SYMP)
print("Duplicated VAERS_ID count");Data_Vax_SYMP[duplicated(VAERS_ID),.N]
print("Not duplicated VAERS_ID count");Data_Vax_SYMP[!duplicated(VAERS_ID),.N]
# remove duplicated VAERS_ID...still a little hazy on the what/why of dual VAERS_ID entries:
Data_Vax_SYMP_2021 <- Data_Vax_SYMP
mergeDVS <- Data_Vax_SYMP[!duplicated(VAERS_ID),]
mergeDVS2021 <- mergeDVS
mergeDVS <-rbind(mergeDVS2020,mergeDVS2021)
mergeData_Vax_SYMP <- rbind(Data_Vax_SYMP_2020,Data_Vax_SYMP_2021)

setnames(merge(mergeDVS[DIED == "Y",.N,.(VAX_TYPE)],mergeDVS[DIED != "Y" ,.N,.(VAX_TYPE)],by="VAX_TYPE"),c("VAX_TYPE","DIED","Other.VAERS.LOG"))[order(-DIED)]
mergeDVS[DIED == "Y",.N,.(LAB_DATA,VAX_TYPE,SYMPTOM1,SYMPTOM2,SYMPTOM3,SYMPTOM4,SYMPTOM5)]
mergeDVS[VAX_TYPE == "COVID19" & DIED == "Y",.(VAX_TYPE,VAX_MANU,LAB_DATA,VAERS_ID,AGE_YRS,CUR_ILL,VAX_DATE,ONSET_DATE,NUMDAYS,SYMPTOM1,SYMPTOM2,SYMPTOM3,SYMPTOM4,SYMPTOM5)]
mergeDVS[VAX_TYPE == "COVID19" & DIED == "Y",.(VAX_TYPE,VAX_MANU,VAERS_ID,AGE_YRS,VAX_DATE,ONSET_DATE,NUMDAYS,SYMPTOM1,SYMPTOM2,SYMPTOM3,SYMPTOM4,SYMPTOM5)]
mergeDVS[VAX_TYPE == "COVID19" & DIED == "Y",.(VAX_TYPE,VAX_MANU,VAERS_ID,AGE_YRS,VAX_DATE,ONSET_DATE,NUMDAYS,SYMPTOM1,SYMPTOM2,SYMPTOM3,SYMPTOM4,SYMPTOM5)][order(-VAX_DATE,VAERS_ID)][,as.data.frame(.SD)]

dev.new()
mergeDVS[VAX_TYPE == "COVID19" & !is.na(CAGE_YR) & CAGE_YR > 17,.N,.(CAGE_YR)][order(CAGE_YR)][,barplot(N,names.arg=CAGE_YR,col=rainbow(nrow(.SD)))]
dev.new()
mergeDVS[VAX_TYPE == "COVID19" & DIED == "Y" & !is.na(CAGE_YR) & CAGE_YR > 17,.N,.(CAGE_YR)][order(CAGE_YR)][,barplot(N,names.arg=CAGE_YR,col=rainbow(nrow(.SD)))]
#mergeDVS[VAX_TYPE == "COVID19"& L_THREAT == "Y",.(SYMPTOM1,SYMPTOM2,SYMPTOM3,SYMPTOM4,SYMPTOM5)]

# COVID.LifeThreatening Corpus
rm(Life.Threatening)
Life.Threatening <- mergeDVS[VAX_TYPE == "COVID19"& L_THREAT == "Y",.(SYMPTOMS=SYMPTOM1)]
Life.Threatening <- rbind(Life.Threatening,mergeDVS[VAX_TYPE == "COVID19"& L_THREAT == "Y",.(SYMPTOMS=SYMPTOM2)])
Life.Threatening <- rbind(Life.Threatening,mergeDVS[VAX_TYPE == "COVID19"& L_THREAT == "Y",.(SYMPTOMS=SYMPTOM3)])
Life.Threatening <- rbind(Life.Threatening,mergeDVS[VAX_TYPE == "COVID19"& L_THREAT == "Y",.(SYMPTOMS=SYMPTOM4)])
Life.Threatening <- rbind(Life.Threatening,mergeDVS[VAX_TYPE == "COVID19"& L_THREAT == "Y",.(SYMPTOMS=SYMPTOM5)])
mergeDVS[VAX_TYPE == "COVID19"& L_THREAT == "Y",.N,.(CAGE_YR)][order(CAGE_YR)][!is.na(CAGE_YR) & CAGE_YR > 17,barplot(N,names.arg=CAGE_YR,col=rainbow(nrow(.SD)))]

rm(anaph)
anaph <- mergeDVS[grepl("anaph",ignore.case=TRUE,SYMPTOM1),]
anaph <- rbind(anaph,mergeDVS[grepl("anaph",ignore.case=TRUE,SYMPTOM2),])
anaph <- rbind(anaph,mergeDVS[grepl("anaph",ignore.case=TRUE,SYMPTOM3),])
anaph <- rbind(anaph,mergeDVS[grepl("anaph",ignore.case=TRUE,SYMPTOM4),])
anaph <- rbind(anaph,mergeDVS[grepl("anaph",ignore.case=TRUE,SYMPTOM5),])
anaph <- anaph[!duplicated(VAERS_ID),]

anaph.COVID19 <- anaph[VAX_TYPE == "COVID19",
.(VAX_TYPE,VAX_MANU,VAERS_ID,AGE_YRS,VAX_DATE,ONSET_DATE,NUMDAYS,DIED,SYMPTOM1,SYMPTOM2,SYMPTOM3,SYMPTOM4,SYMPTOM5)][order(-VAX_DATE,VAERS_ID)]

rm(thromb)
thromb <-  mergeDVS[grepl("thromb",ignore.case=TRUE,SYMPTOM1),]
thromb <- rbind(thromb,mergeDVS[grepl("thromb",ignore.case=TRUE,SYMPTOM2),])
thromb <- rbind(thromb,mergeDVS[grepl("thromb",ignore.case=TRUE,SYMPTOM3),])
thromb <- rbind(thromb,mergeDVS[grepl("thromb",ignore.case=TRUE,SYMPTOM4),])
thromb <- rbind(thromb,mergeDVS[grepl("thromb",ignore.case=TRUE,SYMPTOM5),])
thromb <- thromb[!duplicated(VAERS_ID),]

thromb.COVID19 <- thromb[VAX_TYPE == "COVID19",
.(VAX_TYPE,VAX_MANU,VAERS_ID,AGE_YRS,DIED,VAX_DATE,ONSET_DATE,NUMDAYS,SYMPTOM1,SYMPTOM2,SYMPTOM3,SYMPTOM4,SYMPTOM5)][order(-VAX_DATE,VAERS_ID)]

rbind(anaph,thromb)[,.N,.(VAX_TYPE,SYMPTOM1,SYMPTOM2,SYMPTOM3,SYMPTOM4,SYMPTOM5)]

anaph.thromb <- setnames(merge(anaph[,.N,.(VAX_TYPE)],thromb[,.N,.(VAX_TYPE)],by="VAX_TYPE"),
c("VAX_TYPE","anaph","thromb"))[order(-(anaph + thromb))]

rm(card)
card <-  mergeDVS[grepl("card",ignore.case=TRUE,SYMPTOM1),]
card <- rbind(card,mergeDVS[grepl("card",ignore.case=TRUE,SYMPTOM2),])
card <- rbind(card,mergeDVS[grepl("card",ignore.case=TRUE,SYMPTOM3),])
card <- rbind(card,mergeDVS[grepl("card",ignore.case=TRUE,SYMPTOM4),])
card <- rbind(card,mergeDVS[grepl("card",ignore.case=TRUE,SYMPTOM5),])
card <- card[!duplicated(VAERS_ID),]

rm(lymph)
lymph <-  mergeDVS[grepl("lymph",ignore.case=TRUE,SYMPTOM1),]
lymph <- rbind(lymph,mergeDVS[grepl("lymph",ignore.case=TRUE,SYMPTOM2),])
lymph <- rbind(lymph,mergeDVS[grepl("lymph",ignore.case=TRUE,SYMPTOM3),])
lymph <- rbind(lymph,mergeDVS[grepl("lymph",ignore.case=TRUE,SYMPTOM4),])
lymph <- rbind(lymph,mergeDVS[grepl("lymph",ignore.case=TRUE,SYMPTOM5),])
lymph <- lymph[!duplicated(VAERS_ID),]

rbind(card,lymph)[,.N,.(VAX_TYPE,SYMPTOM1,SYMPTOM2,SYMPTOM3,SYMPTOM4,SYMPTOM5)]
card.lymph <- setnames(merge(card[,.N,.(VAX_TYPE)],lymph[,.N,.(VAX_TYPE)],by="VAX_TYPE"),c("VAX_TYPE","card","lymph"))[order(-(card+lymph))]
death.other <- setnames(merge(mergeDVS[DIED == "Y",.N,.(VAX_TYPE)],mergeDVS[DIED != "Y" ,.N,.(VAX_TYPE)],by="VAX_TYPE"),c("VAX_TYPE","DIED","Other.VAERS.LOG"))[order(-DIED)]

# merge(merge(death.other,card.lymph,all=TRUE,by="VAX_TYPE"),anaph.thromb,all=TRUE,by="VAX_TYPE") [order(-(card+lymph+anaph+thromb))]

# Blood beta-D-glucan positive
rm(glucan)
glucan <-  mergeDVS[grepl("glucan",ignore.case=TRUE,SYMPTOM1),]
glucan <- rbind(glucan,mergeDVS[grepl("glucan",ignore.case=TRUE,SYMPTOM2),])
glucan <- rbind(glucan,mergeDVS[grepl("glucan",ignore.case=TRUE,SYMPTOM3),])
glucan <- rbind(glucan,mergeDVS[grepl("glucan",ignore.case=TRUE,SYMPTOM4),])
glucan <- rbind(glucan,mergeDVS[grepl("glucan",ignore.case=TRUE,SYMPTOM5),])
glucan <- glucan[!duplicated(VAERS_ID),]

rm(blood)
blood <-  mergeDVS[grepl("blood",ignore.case=TRUE,SYMPTOM1),]
blood <- rbind(blood,mergeDVS[grepl("blood",ignore.case=TRUE,SYMPTOM2),])
blood <- rbind(blood,mergeDVS[grepl("blood",ignore.case=TRUE,SYMPTOM3),])
blood <- rbind(blood,mergeDVS[grepl("blood",ignore.case=TRUE,SYMPTOM4),])
blood <- rbind(blood,mergeDVS[grepl("blood",ignore.case=TRUE,SYMPTOM5),])
blood <- blood[!duplicated(VAERS_ID),]

rm(fung)
fung <-  mergeDVS[grepl("fung",ignore.case=TRUE,SYMPTOM1),]
fung <- rbind(fung,mergeDVS[grepl("fung",ignore.case=TRUE,SYMPTOM2),])
fung <- rbind(fung,mergeDVS[grepl("fung",ignore.case=TRUE,SYMPTOM3),])
fung <- rbind(fung,mergeDVS[grepl("fung",ignore.case=TRUE,SYMPTOM4),])
fung <- rbind(fung,mergeDVS[grepl("fung",ignore.case=TRUE,SYMPTOM5),])
fung <- fung[!duplicated(VAERS_ID),]

rm(throat)
throat <-  mergeDVS[grepl("throat",ignore.case=TRUE,SYMPTOM1),]
throat <- rbind(throat,mergeDVS[grepl("throat",ignore.case=TRUE,SYMPTOM2),])
throat <- rbind(throat,mergeDVS[grepl("throat",ignore.case=TRUE,SYMPTOM3),])
throat <- rbind(throat,mergeDVS[grepl("throat",ignore.case=TRUE,SYMPTOM4),])
throat <- rbind(throat,mergeDVS[grepl("throat",ignore.case=TRUE,SYMPTOM5),])
throat <- throat[!duplicated(VAERS_ID),]

# head
rm(head)
head <- mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM1),]
head <- rbind(head,mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM2),])
head <- rbind(head,mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM3),])
head <- rbind(head,mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM4),])
head <- rbind(head,mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM5),])
head <- head[!duplicated(VAERS_ID)]

#chill
rm(chill)
chill <-  mergeDVS[grepl("chill",ignore.case=TRUE,SYMPTOM1),]
chill <- rbind(chill,mergeDVS[grepl("chill",ignore.case=TRUE,SYMPTOM2),])
chill <- rbind(chill,mergeDVS[grepl("chill",ignore.case=TRUE,SYMPTOM3),])
chill <- rbind(chill,mergeDVS[grepl("chill",ignore.case=TRUE,SYMPTOM4),])
chill <- rbind(chill,mergeDVS[grepl("chill",ignore.case=TRUE,SYMPTOM5),])
chill <- chill[!duplicated(VAERS_ID),]

# Guillain-Barre
rm(Guillain.Barre)
Guillain.Barre <- mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM1),]
Guillain.Barre <- rbind(Guillain.Barre,mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM2),])
Guillain.Barre <- rbind(Guillain.Barre,mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM3),])
Guillain.Barre <- rbind(Guillain.Barre,mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM4),])
Guillain.Barre <- rbind(Guillain.Barre,mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM5),])
Guillain.Barre <- Guillain.Barre[!duplicated(VAERS_ID)]

rm(infection)
infection <-  mergeDVS[grepl("infection",ignore.case=TRUE,SYMPTOM1),]
infection <- rbind(infection,mergeDVS[grepl("infection",ignore.case=TRUE,SYMPTOM2),])
infection <- rbind(infection,mergeDVS[grepl("infection",ignore.case=TRUE,SYMPTOM3),])
infection <- rbind(infection,mergeDVS[grepl("infection",ignore.case=TRUE,SYMPTOM4),])
infection <- rbind(infection,mergeDVS[grepl("infection",ignore.case=TRUE,SYMPTOM5),])
infection <- infection[!duplicated(VAERS_ID),]

# neuro
rm(neuro)
neuro <- mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM1),]
neuro <- rbind(neuro,mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM2),])
neuro <- rbind(neuro,mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM3),])
neuro <- rbind(neuro,mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM4),])
neuro <- rbind(neuro,mergeDVS[grepl("Guillain-Barre",ignore.case=TRUE,SYMPTOM5),])
neuro <- neuro[!duplicated(VAERS_ID)

#ceph
rm(ceph)
ceph <-  mergeDVS[grepl("ceph",ignore.case=TRUE,SYMPTOM1),]
ceph <- rbind(ceph,mergeDVS[grepl("ceph",ignore.case=TRUE,SYMPTOM2),])
ceph <- rbind(ceph,mergeDVS[grepl("ceph",ignore.case=TRUE,SYMPTOM3),])
ceph <- rbind(ceph,mergeDVS[grepl("ceph",ignore.case=TRUE,SYMPTOM4),])
ceph <- rbind(ceph,mergeDVS[grepl("ceph",ignore.case=TRUE,SYMPTOM5),])
ceph <- ceph[!duplicated(VAERS_ID),]

death.other <- setnames(merge(mergeDVS[DIED == "Y",.N,.(VAX_TYPE)],mergeDVS[DIED != "Y" ,.N,.(VAX_TYPE)],all=TRUE,by="VAX_TYPE"),c("VAX_TYPE","DIEDeqYES","Other.VAERS.LOG"))[order(-DIEDeqYES)]
life.threat <- setnames(merge(mergeDVS[L_THREAT == "Y",.N,.(VAX_TYPE)],mergeDVS[DIED == "Y" & L_THREAT == "Y" ,.N,.(VAX_TYPE)],all=TRUE,by="VAX_TYPE"),c("VAX_TYPE","L_THREATeqYES","DIEDandL_THREATeqYES"))[order(-L_THREATeqYES)]
blood.card <- setnames(merge(blood[,.N,.(VAX_TYPE)],card[,.N,.(VAX_TYPE)],all=TRUE,by="VAX_TYPE"),c("VAX_TYPE","blood","card"))[order(-(blood+card))]
lymph.throat <- setnames(merge(lymph[,.N,.(VAX_TYPE)],throat[,.N,.(VAX_TYPE)],all=TRUE,by="VAX_TYPE"),
c("VAX_TYPE","lymph","throat"))[order(-(lymph + throat))]
anaph.thromb <- setnames(merge(anaph[,.N,.(VAX_TYPE)],thromb[,.N,.(VAX_TYPE)],all=TRUE,by="VAX_TYPE"),
c("VAX_TYPE","anaph","thromb"))[order(-(anaph + thromb))]
head.chill <- setnames(merge(head[,.N,.(VAX_TYPE)],chill[,.N,.(VAX_TYPE)],all=TRUE,by="VAX_TYPE"),
c("VAX_TYPE","head","chill"))[order(-(head + chill))]
ceph.neuro <- setnames(merge(ceph[,.N,.(VAX_TYPE)],neuro[,.N,.(VAX_TYPE)],all=TRUE,by="VAX_TYPE"),
c("VAX_TYPE","ceph","neuro"))[order(-(neuro + ceph))]
# not working yet
# cere.chest <- setnames(merge(cere[,.N,.(VAX_TYPE)],chest[,.N,.(VAX_TYPE)],all=TRUE,by="VAX_TYPE"),
# c("VAX_TYPE","cere","chest"))[order(-(cere + chest))]
GBS.infection <- setnames(merge(infection[,.N,.(VAX_TYPE)],Guillain.Barre[,.N,.(VAX_TYPE)],all=TRUE,by="VAX_TYPE"),
c("VAX_TYPE","Guillain.Barre","infection"))[order(-(Guillain.Barre + infection))]
fung.glucan <- setnames(merge(fung[,.N,.(VAX_TYPE)],glucan[,.N,.(VAX_TYPE)],all=TRUE,by="VAX_TYPE"),
c("VAX_TYPE","fung","glucan"))[order(-(fung + glucan))]


m1 <- merge(death.other,life.threat,all=TRUE,by="VAX_TYPE")
m2 <- merge(blood.card,lymph.throat,all=TRUE,by="VAX_TYPE")
m3 <- merge(anaph.thromb,ceph.neuro,all=TRUE,by="VAX_TYPE")
m4 <- merge(GBS.infection,fung.glucan,all=TRUE,by="VAX_TYPE")

m5 <- merge(m1,m2,all=TRUE,by="VAX_TYPE")
m6 <- merge(m3,m4,all=TRUE,by="VAX_TYPE")
m7 <- merge(m5,m6,all=TRUE,by="VAX_TYPE")

m8 <- m7[order(-Other.VAERS.LOG)];m8[is.na(m8)] <-0
m8


# scratch

cat(' 
print("list of errors in the data")
cat('
Errors in the Data (Data discrepancies)
mergeDVS[VAX_TYPE == "COVID19",.N,.(VAX_DATE,ONSET_DATE,NUMDAYS)][order(-NUMDAYS)][1:100]
       VAX_DATE ONSET_DATE NUMDAYS N
  1: 09/16/1966 12/23/2020   19822 1
  2: 06/06/1969 12/22/2020   18827 1
  3: 12/18/1969 12/18/2020   18628 1
  4: 07/23/1970 12/18/2020   18411 1
  5: 04/22/2019 12/23/2020     611 1
  6: 01/02/2020 01/04/2021     368 1
  7: 01/04/2020 01/05/2021     367 1
  8: 01/04/2020 01/04/2021     366 1
  9: 01/04/2020 12/30/2020     361 1
 10: 02/18/2020 12/18/2020     304 1

')
cat('
mergeDVS[VAX_TYPE == "COVID19" & AGE_YRS != CAGE_YR & AGE_YRS < 15,.N,.(AGE_YRS,CAGE_YR)][order(-N)]
    AGE_YRS CAGE_YR  N
 1:    1.08       0 18
 2:    0.33      59  1
 3:    0.50      43  1
 4:    0.58      38  1
 5:    0.25      44  1
 6:    0.42       0  1
 7:    1.58       0  1
 8:    1.25       0  1
 9:    1.75       0  1
10:    8.00       7  1
11:    1.50       0  1
12:    0.08       0  1
13:    1.33       1  1
 ')
