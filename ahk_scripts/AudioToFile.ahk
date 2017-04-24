stdout := FileOpen("AudioLevel.txt", "w `n")
SoundGet, N
stdout.Write(N)