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
// Highlighting text that matches the selection
//
// Defines an option highlightSelectionMatches, which, when enabled,
// will style strings that match the selection throughout the
// document.
//
// The option can be set to true to simply enable it, or to a
// {minChars, style} object to explicitly configure it. minChars is
// the minimum amount of characters that should be selected for the
// behavior to occur, and style is the token style to apply to the
// matches. This will be prefixed by "cm-" to create an actual CSS
// class name.

(function() {
  var DEFAULT_MIN_CHARS = 2;
  var DEFAULT_TOKEN_STYLE = "matchhighlight";

  function State(options) {
    this.minChars = typeof options == "object" && options.minChars || DEFAULT_MIN_CHARS;
    this.style = typeof options == "object" && options.style || DEFAULT_TOKEN_STYLE;
    this.overlay = null;
  }

  CodeMirror.defineOption("highlightSelectionMatches", false, function(cm, val, old) {
    var prev = old && old != CodeMirror.Init;
    if (val && !prev) {
      cm.state.matchHighlighter = new State(val);
      cm.on("cursorActivity", highlightMatches);
    } else if (!val && prev) {
      var over = cm.state.matchHighlighter.overlay;
      if (over) cm.removeOverlay(over);
      cm.state.matchHighlighter = null;
      cm.off("cursorActivity", highlightMatches);
    }
  });

  function highlightMatches(cm) {
    cm.operation(function() {
      var state = cm.state.matchHighlighter;
      if (state.overlay) {
        cm.removeOverlay(state.overlay);
        state.overlay = null;
      }

      if (!cm.somethingSelected()) return;
      var selection = cm.getSelection().replace(/^\s+|\s+$/g, "");
      if (selection.length < state.minChars) return;

      cm.addOverlay(state.overlay = makeOverlay(selection, state.style));
    });
  }

  function makeOverlay(query, style) {
    return {token: function(stream) {
      if (stream.match(query)) return style;
      stream.next();
      stream.skipTo(query.charAt(0)) || stream.skipToEnd();
    }};
  }
})();
