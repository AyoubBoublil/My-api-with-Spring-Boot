package org.project.smarttrack.service.mailing;

public interface IMailingService {

    String sendActivateAccountEmail(String to, String subject, String text);

}
