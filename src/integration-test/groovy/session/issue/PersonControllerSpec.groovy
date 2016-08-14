package session.issue

import grails.plugins.rest.client.RestBuilder
import grails.test.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Value
import spock.lang.Specification

/**
 * Created by goran on 2016-08-14.
 */
@Integration(applicationClass = Application)
class PersonControllerSpec extends Specification {

    @Value('${local.server.port}')
    Integer serverPort


    void "list"() {
        given:
        RestBuilder rest = new RestBuilder()

        when:
        def response = rest.get("http://localhost:${serverPort}/persons?tenant=1")

        then:
        response.status == 200

        and:
        response.json == []
    }

    void "create person without transactional controller"() {
        given:
        RestBuilder rest = new RestBuilder()
        def payload = [firstName: 'One', lastName: 'Person']

        when:
        def response = rest.post("http://localhost:${serverPort}/persons?tenant=1") {
            contentType "application/json"
            json payload
        }

        then:
        response.status == 201 // CREATED

        and:
        response.json.tenantId == "1"
        response.json.firstName == "One"
        response.json.lastName == "Person"
    }

    void "create person with transactional controller"() {
        given:
        RestBuilder rest = new RestBuilder()
        def payload = [firstName: 'Two', lastName: 'Person']

        when:
        def response = rest.post("http://localhost:${serverPort}/person/save2?tenant=2") {
            contentType "application/json"
            json payload
        }

        then:
        response.status == 201 // CREATED

        and:
        response.json.tenantId == "2"
        response.json.firstName == "Two"
        response.json.lastName == "Person"
    }

    void "create person without withCurrent block"() {
        given:
        RestBuilder rest = new RestBuilder()
        def payload = [firstName: 'Three', lastName: 'Person']

        when:
        def response = rest.post("http://localhost:${serverPort}/person/save3?tenant=3") {
            contentType "application/json"
            json payload
        }

        then:
        response.status == 201 // CREATED

        and:
        response.json.tenantId == "3"
        response.json.firstName == "Three"
        response.json.lastName == "Person"
    }
}
