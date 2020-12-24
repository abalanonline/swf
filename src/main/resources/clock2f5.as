now = new Date();
i = now.getHours();
if (i < "10")
{
  i = "0" add i;
}
timeHours = i;
s = now.getMinutes();
if (s < "10")
{
  s = "0" add s;
}
timeMinutes = s;
i = i add ":" add s;
timeHoursMinutes = i;
s = now.getSeconds();
if (s < "10")
{
  s = "0" add s;
}
timeSeconds = s;
timeHoursMinutesSeconds = i add ":" add s;

batteryLevel = "";
