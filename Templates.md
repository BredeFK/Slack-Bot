# JSON Templates
This is a collection of all json templates used in the project. The templates is how the json's look like when getting posted to slacks api.

## Quote
```JSON
{
  "channel": "<ChannelID>",
  "attachments": [
    {
      "blocks": [
        {
          "type": "section",
          "text": {
            "type": "mrkdwn",
            "text": "_<quote>_\n\n- *<Author>*"
          }
        },
        {
          "type": "context",
          "elements": [
            {
              "type": "mrkdwn",
              "text": "*Last updated:* <date>\n:copyright: <copyright>\n"
            }
          ]
        }
      ]
    }
  ]
}
```


## GitHubUser
```JSON
{
  "channel": "<ChannelID>",
  "attachments": [
    {
      "blocks": [
        {
          "type": "section",
          "text": {
            "type": "mrkdwn",
            "text": "*<name>*\n<login>\n\n\n_<bio>_\n:briefcase: <company>\n:house: <loation>"
          },
          "accessory": {
            "type": "image",
            "image_url": "<avatar_url>",
            "alt_text": "avatar-<login>"
          }
        },
        {
          "type": "section",
          "text": {
            "type": "mrkdwn",
            "text": "Repositories"
          },
          "accessory": {
            "type": "static_select",
            "placeholder": {
              "type": "plain_text",
              "text": "Select a repository",
              "emoji": true
            },
            "options": [
              {
                "text": {
                  "type": "plain_text",
                  "text": "Repository 1",
                  "emoji": true
                },
                "value": "value-0"
              },
              {
                "text": {
                  "type": "plain_text",
                  "text": "Repository 2",
                  "emoji": true
                },
                "value": "value-1"
              },
              {
                "text": {
                  "type": "plain_text",
                  "text": "Repository 3",
                  "emoji": true
                },
                "value": "value-2"
              },
              {...}
            ]
          }
        },
        {
          "type": "context",
          "elements": [
            {
              "type": "mrkdwn",
              "text": "<html_url>"
            }
          ]
        }
      ]
    }
  ]
}
```

## Repository
```JSON
{
  "channel": "<ChannelID>",
  "attachments": [
    {
      "blocks": [
        {
          "type": "section",
          "text": {
            "type": "mrkdwn",
            "text": "`GitHub Repository <full_name>`\n*<name>*\n_Forked from <source.html_url> _\n```<description>```\n:small_blue_diamond:<language>\t:scales: <license.name>\t_Last updated on_ <updated_at>"
          }
        },
        {
          "type": "context",
          "elements": [
            {
              "type": "mrkdwn",
              "text": "<html_url>"
            }
          ]
        }
      ]
    }
  ]
}
```
