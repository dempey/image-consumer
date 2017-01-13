Email inbox to watch note that the email address in the store-ui is encoded and separated from the password by a single colon. Multiple emails can be added here and watched as long as they all point to the same channel, "receiveMail", which in the sample is pulling the constant from the Integrations class, just in case.

```xml
	<int-mail:imap-idle-channel-adapter
		id="image.consumer"
		store-uri="imaps://image.consumer.stg%40gmail.com:STGrocks@imap.gmail.com:993/inbox"
		channel="#{T(com.stg.imageconsumer.integrations.Integrations).RECEIVE_MAIL}"
		auto-startup="true" should-delete-messages="false"
		java-mail-properties="javaImpasMailProperties" />
```