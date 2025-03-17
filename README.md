## Typechecker for Stella language
### How to run
```shell
git clone https://github.com/ArsenyBochkarev/stella-typechecker.git
cd stella-typechecker
sbt compile
./stella-typechecker <stdin program text>
<press Ctrl+D after input>
```

*Note*: don't forget to leave one blank line before EOF

### Output
If the error was found, the typechecker will print error to stderr and return non-zero exit code.

If everything's fine, you'll see messages about processed (checked) functions.