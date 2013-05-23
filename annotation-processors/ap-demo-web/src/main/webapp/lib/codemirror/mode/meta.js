/*
 * Copyright (c) 2013, Klaus Boeing & Michel Graciano.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the project's authors nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND/OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
CodeMirror.modeInfo = [
  {name: 'APL', mime: 'text/apl', mode: 'apl'},
  {name: 'Asterisk', mime: 'text/x-asterisk', mode: 'asterisk'},
  {name: 'C', mime: 'text/x-csrc', mode: 'clike'},
  {name: 'C++', mime: 'text/x-c++src', mode: 'clike'},
  {name: 'Cobol', mime: 'text/x-cobol', mode: 'cobol'},
  {name: 'Java', mime: 'text/x-java', mode: 'clike'},
  {name: 'C#', mime: 'text/x-csharp', mode: 'clike'},
  {name: 'Scala', mime: 'text/x-scala', mode: 'clike'},
  {name: 'Clojure', mime: 'text/x-clojure', mode: 'clojure'},
  {name: 'CoffeeScript', mime: 'text/x-coffeescript', mode: 'coffeescript'},
  {name: 'Common Lisp', mime: 'text/x-common-lisp', mode: 'commonlisp'},
  {name: 'CSS', mime: 'text/css', mode: 'css'},
  {name: 'D', mime: 'text/x-d', mode: 'd'},
  {name: 'diff', mime: 'text/x-diff', mode: 'diff'},
  {name: 'ECL', mime: 'text/x-ecl', mode: 'ecl'},
  {name: 'Erlang', mime: 'text/x-erlang', mode: 'erlang'},
  {name: 'Gas', mime: 'text/x-gas', mode: 'gas'},
  {name: 'GitHub Flavored Markdown', mode: 'gfm'},
  {name: 'GO', mime: 'text/x-go', mode: 'go'},
  {name: 'Groovy', mime: 'text/x-groovy', mode: 'groovy'},
  {name: 'Haskell', mime: 'text/x-haskell', mode: 'haskell'},
  {name: 'Haxe', mime: 'text/x-haxe', mode: 'haxe'},
  {name: 'ASP.NET', mime: 'application/x-aspx', mode: 'htmlembedded'},
  {name: 'Embedded Javascript', mime: 'application/x-ejs', mode: 'htmlembedded'},
  {name: 'JavaServer Pages', mime: 'application/x-jsp', mode: 'htmlembedded'},
  {name: 'HTML', mime: 'text/html', mode: 'htmlmixed'},
  {name: 'HTTP', mime: 'message/http', mode: 'http'},
  {name: 'JavaScript', mime: 'text/javascript', mode: 'javascript'},
  {name: 'JSON', mime: 'application/x-json', mode: 'javascript'},
  {name: 'JSON', mime: 'application/json', mode: 'javascript'},
  {name: 'TypeScript', mime: 'application/typescript', mode: 'javascript'},
  {name: 'Jinja2', mime: 'jinja2', mode: 'jinja2'},
  {name: 'LESS', mime: 'text/x-less', mode: 'less'},
  {name: 'LiveScript', mime: 'text/x-livescript', mode: 'livescript'},
  {name: 'Lua', mime: 'text/x-lua', mode: 'lua'},
  {name: 'Markdown (GitHub-flavour)', mime: 'text/x-markdown', mode: 'markdown'},
  {name: 'mIRC', mime: 'text/mirc', mode: 'mirc'},
  {name: 'NTriples', mime: 'text/n-triples', mode: 'ntriples'},
  {name: 'OCaml', mime: 'text/x-ocaml', mode: 'ocaml'},
  {name: 'Pascal', mime: 'text/x-pascal', mode: 'pascal'},
  {name: 'Perl', mime: 'text/x-perl', mode: 'perl'},
  {name: 'PHP', mime: 'text/x-php', mode: 'php'},
  {name: 'PHP(HTML)', mime: 'application/x-httpd-php', mode: 'php'},
  {name: 'Pig', mime: 'text/x-pig', mode: 'pig'},
  {name: 'Plain Text', mime: 'text/plain', mode: 'null'},
  {name: 'Properties files', mime: 'text/x-properties', mode: 'clike'},
  {name: 'Python', mime: 'text/x-python', mode: 'python'},
  {name: 'R', mime: 'text/x-rsrc', mode: 'r'},
  {name: 'reStructuredText', mime: 'text/x-rst', mode: 'rst'},
  {name: 'Ruby', mime: 'text/x-ruby', mode: 'ruby'},
  {name: 'Rust', mime: 'text/x-rustsrc', mode: 'rust'},
  {name: 'Sass', mime: 'text/x-sass', mode: 'sass'},
  {name: 'Scheme', mime: 'text/x-scheme', mode: 'scheme'},
  {name: 'SCSS', mime: 'text/x-scss', mode: 'css'},
  {name: 'Shell', mime: 'text/x-sh', mode: 'shell'},
  {name: 'Sieve', mime: 'application/sieve', mode: 'sieve'},
  {name: 'Smalltalk', mime: 'text/x-stsrc', mode: 'smalltalk'},
  {name: 'Smarty', mime: 'text/x-smarty', mode: 'smarty'},
  {name: 'SPARQL', mime: 'application/x-sparql-query', mode: 'sparql'},
  {name: 'SQL', mime: 'text/x-sql', mode: 'sql'},
  {name: 'MariaDB', mime: 'text/x-mariadb', mode: 'sql'},
  {name: 'sTeX', mime: 'text/x-stex', mode: 'stex'},
  {name: 'LaTeX', mime: 'text/x-latex', mode: 'stex'},
  {name: 'Tcl', mime: 'text/x-tcl', mode: 'tcl'},
  {name: 'TiddlyWiki ', mime: 'text/x-tiddlywiki', mode: 'tiddlywiki'},
  {name: 'Tiki wiki', mime: 'text/tiki', mode: 'tiki'},
  {name: 'VB.NET', mime: 'text/x-vb', mode: 'vb'},
  {name: 'VBScript', mime: 'text/vbscript', mode: 'vbscript'},
  {name: 'Velocity', mime: 'text/velocity', mode: 'velocity'},
  {name: 'Verilog', mime: 'text/x-verilog', mode: 'verilog'},
  {name: 'XML', mime: 'application/xml', mode: 'xml'},
  {name: 'HTML', mime: 'text/html', mode: 'xml'},
  {name: 'XQuery', mime: 'application/xquery', mode: 'xquery'},
  {name: 'YAML', mime: 'text/x-yaml', mode: 'yaml'},
  {name: 'Z80', mime: 'text/x-z80', mode: 'z80'}
];
