# TODO

- [ ] Do we need to wait for a `frontend.ready` before we configure the frontend?
      If so should we unsubscribe from the `frontend.ready` messages after receiving one
      (to prevent an infinite loop)?
- [ ] How `message.queue` is supposed to work? Do we need to call it after a `message.send`?
- [ ] It is not clear how `ide.read` is supposed to work. Is this triggered by the web IDE, and
      we're supposed to react to it? What's the point of `files`? Do we return the full path,
      or just the basename?
- [ ] It is not clear what "current patterns" are in `ide.write`'s response. Also how `ide.write`
      is supposed to work? Is this triggered by the frontend and we're supposed to reply to it?
- [ ] In `ide.reload` what are "watched files"? When do we send this? Or do we just listen to this?
- [ ] Same with `ide.write`. Do we get this from the web IDE?
- [ ] `content` of `ide.write` should be required
- [ ] Which console does `console.read` read? The one in the web IDE?
- [ ] Where is the actual terminal running? Is this the responsibility of the SDK?
- [ ] What is the relationship between `process.log.set` and `process.log.new`? Do I have to call
      `set` before `new`? What will I get if I call `new` before `set`?
- [ ] How does `fsm.update` work? Is this something content creators can use to query the state of
      the fsm or does the framework query us? Also the keys are snake_case instead of camelCase
- [ ] `dashboard.reloadIframe` is not documented