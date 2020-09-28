i = "12";
i = fscommand2 ("GetTimeHours");
if (i < "10")
{
  i = "0" add i;
}
timeHours = i;
s = "35";
s = fscommand2 ("GetTimeMinutes");
if (s < "10")
{
  s = "0" add s;
}
timeMinutes = s;
i = i add ":" add s;
timeHoursMinutes = i;
s = "55";
s = fscommand2 ("GetTimeSeconds");
if (s < "10")
{
  s = "0" add s;
}
timeSeconds = s;
timeHoursMinutesSeconds = i add ":" add s;
i = "4";
i = fscommand2 ("GetBatteryLevel");
s = "";
while ("0" < i) {
  s = s add "I";
  i = i - 1;
}
batteryLevel = s;
