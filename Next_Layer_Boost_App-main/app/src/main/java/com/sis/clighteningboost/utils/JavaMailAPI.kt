package com.sis.clighteningboost.utils

import android.os.AsyncTask
import android.app.ProgressDialog
import android.content.Context
import java.util.*
import javax.mail.*
import javax.mail.internet.MimeMessage
import javax.mail.internet.InternetAddress

class JavaMailAPI     //Constructor
    (//Add those line in dependencies
    //implementation files('libs/activation.jar')
    //implementation files('libs/additionnal.jar')
    //implementation files('libs/mail.jar')
    //Need INTERNET permission
    //Variables
    private val mContext: Context,
    private val mEmail: String,
    private val mSubject: String,
    private val mMessage: String
) : AsyncTask<Void?, Void?, Void?>() {
    private var mSession: Session? = null
    private val mProgressDialog: ProgressDialog? = null
    override fun onPreExecute() {
        super.onPreExecute()
        //Show progress dialog while sending email
        // Toast.makeText(mContext,"Sending Email To Admin",Toast.LENGTH_LONG).show();
//        mProgressDialog = ProgressDialog.show(mContext,"Sending message", "Please wait...",false,false);
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        //Dismiss progress dialog when message successfully send
//        mProgressDialog.dismiss();
        //Show success toast
        // Toast.makeText(mContext,"Email Sent",Toast.LENGTH_SHORT).show();
    }

    protected override fun doInBackground(vararg params: Void?): Void? {
        //Creating properties
        val props = Properties()
        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
        //Creating a new session
        mSession = Session.getDefaultInstance(props,
            object : Authenticator() {
                //Authenticating the password
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(Utils.myEmail, Utils.myEmailPassword)
                }
            })
        try {
            //Creating MimeMessage object
            val mm = MimeMessage(mSession)
            //Setting sender address
            mm.setFrom(InternetAddress(Utils.myEmail))
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, InternetAddress(mEmail))
            //Adding subject
            mm.subject = mSubject
            //Adding message
            mm.setText(mMessage)
            //Sending email
            Transport.send(mm)
            //            BodyPart messageBodyPart = new MimeBodyPart();
//
//            messageBodyPart.setText(message);
//
//            Multipart multipart = new MimeMultipart();
//
//            multipart.addBodyPart(messageBodyPart);
//
//            messageBodyPart = new MimeBodyPart();
//
//            DataSource source = new FileDataSource(filePath);
//
//            messageBodyPart.setDataHandler(new DataHandler(source));
//
//            messageBodyPart.setFileName(filePath);
//
//            multipart.addBodyPart(messageBodyPart);

//            mm.setContent(multipart);
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
        return null
    }


}