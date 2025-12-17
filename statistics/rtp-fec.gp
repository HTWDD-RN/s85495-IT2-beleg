#!/usr/local/bin/gnuplot --persist

THIRD=ARG3
print "script name        : ", ARG0
print "first argument     : ", ARG1
print "third argument     : ", THIRD
print "number of arguments: ", ARGC

ARG1="result.txt"

set title "Ausfallstellenkorrektur mittels Parity-Check-Codes"
#set key title "Parameter"
set xlabel "P_e (Kanal)"
set ylabel "P_e (Bild)"
set grid
set key right bottom
#
# Dummy, korrekte Formel muss ermittelt werden
#f(x,a)=a*0.01+x
## Probability function for image loss based on channel error probability (x) and RTP packets per image (a)
f(x,a)= 1 - (1 - x)**a


set xrange [0:1]
set yrange [0:1]
#plot ARG1 with line linewidth 3 title ARG2

plot ARG1 using 1:2 with line linewidth 3 title "1 Frame/B."
replot f(x,2) lw 2 title "2 Frame/B."
replot f(x,5) lw 2 title "5 Frame/B."
# FEC
replot ARG1 using 1:3  with line  linewidth 3  title "PCC k=  2"
#replot ARG1 using 1:4  w l  lw 3  title "PCC k=  3"
#replot ARG1 using 1:5  w l  lw 3  title "PCC k=  6"
#replot ARG1 using 1:6  w l  lw 3  title "PCC k=12"
#replot ARG1 using 1:7  w l  lw 3  title "PCC k=24"
#replot ARG1 using 1:8  w l  lw 3  title "PCC k=48"
 replot ARG1 using 1:4 with lines linewidth 3 title "PCC k=3"
 replot ARG1 using 1:5 with lines linewidth 3 title "PCC k=6"
 replot ARG1 using 1:6 with lines linewidth 3 title "PCC k=12"
 replot ARG1 using 1:7 with lines linewidth 3 title "PCC k=24"
 replot ARG1 using 1:8 with lines linewidth 3 title "PCC k=48"
# notitle
pause -1
